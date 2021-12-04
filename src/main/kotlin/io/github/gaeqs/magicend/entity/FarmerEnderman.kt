package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.ai.defaults.canReachBlock
import io.github.gaeqs.magicend.ai.defaults.findPointOfInterest
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.statemachine.builder.StateMachineBuilder
import io.github.gaeqs.magicend.ai.statemachine.node.lambda
import io.github.gaeqs.magicend.ai.statemachine.node.tree
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*
import io.github.gaeqs.magicend.block.ChorusWheat
import io.github.gaeqs.magicend.block.entity.EnderBreadPlateBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.GlobalPos
import net.minecraft.world.World

class FarmerEnderman(type: EntityType<out FarmerEnderman>, world: World) : EnderVillager(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "farmer_enderman")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<FarmerEnderman> { type, world -> FarmerEnderman(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 3.0f)).build()

        val MAX_CROPS = 6

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
        }
    }

    private var crops = 0
    private var status: FarmerStatus = FarmerStatus.NORMAL

    init {
        initAI()
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        crops = nbt.getInt("crops")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("crops", crops)
    }

    private fun changeStatus(status: FarmerStatus) {
        if (status == this.status) return
        this.status = status
        val values = FarmerStatus.values().map { it.activityName }
        ai.activities.removeIf { it.name in values }
        when (status) {
            FarmerStatus.RUNNING_AWAY -> addRunAwayActivity()
            FarmerStatus.NORMAL -> addNormalActivity()
        }
    }

    private fun initAI() {
        ai.activities += TreeActivity("activity_manager", ai, rootLoopUnconditional {
            and {
                or {
                    and {
                        findNearestLivingEntities()
                        isEntityNear { it is VoidSnake || it is VoidWorm || it is VoidSquid }
                        lambda {
                            tick {
                                changeStatus(FarmerStatus.RUNNING_AWAY)
                                TreeNode.InvocationResult.SUCCESS
                            }
                        }
                    }

                    lambda {
                        tick {
                            changeStatus(FarmerStatus.NORMAL)
                            TreeNode.InvocationResult.SUCCESS
                        }
                    }
                }

                wait(40)
            }
        })
        addNormalActivity()
    }

    private fun addRunAwayActivity() {
        ai.activities += TreeActivity(FarmerStatus.RUNNING_AWAY.activityName, ai, rootLoopUnconditional {
            and {
                or {
                    and {
                        findPointOfInterest(PointOfInterestTypes.ENDER_TABLE, MemoryTypes.POINT_OF_INTEREST, 64)
                        walkToPosition(MemoryTypes.POINT_OF_INTEREST, 2.5f)
                        wait(20)
                    }
                    and {
                        findRandomWalkTarget(2.5f)
                        timed(40, 70) {
                            walkToTarget()
                        }
                    }

                    succeeder {}
                }
                wait(1)
            }
        })
    }

    private fun addNormalActivity() {
        ai.activities += TreeActivity(FarmerStatus.NORMAL.activityName, ai, rootLoopUnconditional {
            // EAT BREAD
            and {
                predicate { health < maxHealth && !hasStatusEffect(StatusEffects.REGENERATION) }
                findPointOfInterest(PointOfInterestTypes.ENDER_BREAD_PLATE, MemoryTypes.POINT_OF_INTEREST, 48) {
                    val entity = world.getBlockEntity(it)
                    entity is EnderBreadPlateBlockEntity && !entity.isEmpty()
                }
                walkToPosition(MemoryTypes.POINT_OF_INTEREST, 2.0f)
                // Position reached successfully. Waiting.
                wait(50)
                succeeder {
                    lambda {
                        tick {
                            val pos = ai.getMemory(MemoryTypes.POINT_OF_INTEREST)
                                ?: return@tick TreeNode.InvocationResult.FAIL

                            val entity = world.getBlockEntity(pos.pos)
                            if (entity !is EnderBreadPlateBlockEntity || entity.isFull())
                                return@tick TreeNode.InvocationResult.FAIL

                            entity.amount--

                            addStatusEffect(
                                StatusEffectInstance(
                                    StatusEffects.REGENERATION,
                                    40,
                                    0
                                )
                            )

                            TreeNode.InvocationResult.SUCCESS
                        }
                    }
                }
            }

            // WORK
            stateMachine {
                initWorkAI()
            }
        })
    }

    private fun StateMachineBuilder.initWorkAI() {
        startNode = "idle"
        lambda("idle") {
            tick {
                val world = world as? ServerWorld ?: return@tick

                val farmland = world.findPointOfInterest(entity.blockPos, PointOfInterestTypes.FARMLAND, 50) {
                    val state = world.getBlockState(it.up())
                    val block = state.block
                    (state.isAir || crops < MAX_CROPS && block is ChorusWheat && block.isMature(state))
                            && canReachBlock(it, 50)
                }


                if (farmland != null) {
                    ai.remember(MemoryTypes.POINT_OF_INTEREST, GlobalPos.create(world.registryKey, farmland))
                    changeState("farming")
                    return@tick
                }

                if (crops >= 3) {
                    val plate =
                        world.findPointOfInterest(entity.blockPos, PointOfInterestTypes.ENDER_BREAD_PLATE, 50) {
                            val entity = world.getBlockEntity(it)
                            entity is EnderBreadPlateBlockEntity && !entity.isFull()
                        }
                    if (plate != null) {
                        ai.remember(MemoryTypes.POINT_OF_INTEREST, GlobalPos.create(world.registryKey, plate))
                        changeState("baking")
                        return@tick
                    }
                }

                changeState("walking")
            }
        }

        tree("walking") {
            root = rootAnd {
                succeeder {
                    and {
                        findRandomWalkTarget(1.5f)
                        timed(60, 100) {
                            walkToTarget()
                        }
                    }
                }
                wait(20)
            }
            success {
                changeState("idle")
            }
        }

        tree("farming") {
            root = rootAnd {
                walkToPosition(MemoryTypes.POINT_OF_INTEREST, 1.5f)
                wait(20)
                isNearPosition(MemoryTypes.POINT_OF_INTEREST)
                lambda {
                    tick {
                        val position = ai.getMemory(MemoryTypes.POINT_OF_INTEREST)
                            ?: return@tick TreeNode.InvocationResult.FAIL
                        val blockPos = position.pos.up()

                        val state = world.getBlockState(blockPos)
                        val block = state.block

                        if (state.isAir) {
                            world.setBlockState(blockPos, ChorusWheat.BLOCK.defaultState, 3)
                            world.playSound(
                                null,
                                blockPos.x.toDouble(),
                                blockPos.y.toDouble(),
                                blockPos.z.toDouble(),
                                SoundEvents.ITEM_CROP_PLANT,
                                SoundCategory.BLOCKS,
                                1.0f,
                                1.0f
                            )

                            return@tick TreeNode.InvocationResult.SUCCESS
                        }

                        if (block is ChorusWheat && block.isMature(state)) {
                            world.breakBlock(blockPos, false, this@FarmerEnderman)
                            crops++
                            return@tick TreeNode.InvocationResult.SUCCESS
                        }

                        TreeNode.InvocationResult.FAIL
                    }
                }
            }
            anyResult {
                changeState("idle")
            }
        }

        tree("baking") {
            root = rootAnd {
                walkToPosition(MemoryTypes.POINT_OF_INTEREST, 1.5f)
                wait(50)
                isNearPosition(MemoryTypes.POINT_OF_INTEREST)
                lambda {
                    tick {
                        if (crops < 3) return@tick TreeNode.InvocationResult.FAIL
                        val position = ai.getMemory(MemoryTypes.POINT_OF_INTEREST)
                            ?: return@tick TreeNode.InvocationResult.FAIL
                        val block = world.getBlockEntity(position.pos)
                        if (block !is EnderBreadPlateBlockEntity) return@tick TreeNode.InvocationResult.FAIL
                        if (block.isFull()) return@tick TreeNode.InvocationResult.FAIL

                        crops -= 3
                        block.amount++

                        TreeNode.InvocationResult.SUCCESS
                    }
                }
            }
            anyResult {
                changeState("idle")
            }
        }
    }
}

private enum class FarmerStatus(val activityName: String) {
    RUNNING_AWAY("running_away"),
    NORMAL("normal");
}
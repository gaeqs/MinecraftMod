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
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.*
import io.github.gaeqs.magicend.block.ChorusWheat
import io.github.gaeqs.magicend.block.entity.EnderBreadPlateBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.nbt.NbtCompound
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
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
        }
    }

    private var crops = 0

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        crops = nbt.getInt("crops")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("crops", crops)
    }

    override fun TreeNodeParentBuilder<*>.initWorkAI() {
        // FARMING
        and {
            findPointOfInterest(PointOfInterestTypes.FARMLAND, MemoryTypes.POINT_OF_INTEREST, 64) {
                val state = world.getBlockState(it.up())
                val block = state.block
                (state.isAir || crops < MAX_CROPS && block is ChorusWheat && block.isMature(state))
                        && canReachBlock(it, 50)
            }

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

        // BAKING
        and {
            predicate { crops >= 3 }
            findPointOfInterest(PointOfInterestTypes.ENDER_BREAD_PLATE, MemoryTypes.POINT_OF_INTEREST, 64) {
                val entity = world.getBlockEntity(it)
                entity is EnderBreadPlateBlockEntity && !entity.isFull()
            }

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

        and {
            succeeder {
                and {
                    findRandomWalkTargetToPoint(1.5f, PointOfInterestTypes.ENDER_TABLE)
                    timed(60, 100) {
                        walkToTarget()
                    }
                }
            }
            wait(20)
        }

    }
}
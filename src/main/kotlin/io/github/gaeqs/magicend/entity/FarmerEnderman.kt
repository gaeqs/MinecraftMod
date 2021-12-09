package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.ai.defaults.canReachBlock
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.*
import io.github.gaeqs.magicend.block.ChorusWheatBlock
import io.github.gaeqs.magicend.block.entity.EnderBreadPlateBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemGroup
import net.minecraft.item.SpawnEggItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.dynamic.GlobalPos
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class FarmerEnderman(type: EntityType<out FarmerEnderman>, world: World) : EnderVillager(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "farmer_enderman")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<FarmerEnderman> { type, world -> FarmerEnderman(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 2.9f)).build()

        val EGG_ITEM_IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "farmer_enderman_spawn_egg")
        val EGG_ITEM = SpawnEggItem(
            ENTITY_TYPE, 0x161616, 0x00800d,
            FabricItemSettings().group(ItemGroup.MISC)
        )

        val MAX_CROPS = 6

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 128.0)
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

    override fun remove() {
        super.remove()
        val point = ai.getMemory(MemoryTypes.POINT_OF_INTEREST) ?: return
        village.releaseFarmland(point)
    }

    override fun TreeNodeParentBuilder<*>.initWorkAI() {
        // FARMING
        and {
            succeeder {
                setOccupied(MemoryTypes.POINT_OF_INTEREST, false)
            }

            findPointOfInterest(PointOfInterestTypes.FARMLAND, MemoryTypes.POINT_OF_INTEREST, 64) {
                val state = world.getBlockState(it.up())
                val block = state.block
                (state.isAir || crops < MAX_CROPS && block is ChorusWheatBlock && block.isMature(state))
                        && !village.isFarmlandOccupied(this@FarmerEnderman, GlobalPos.create(world.registryKey, it))
                        && canReachBlock(it, 50)
            }

            or {
                and {
                    walkToPosition(MemoryTypes.POINT_OF_INTEREST, 1.5f)
                    wait(20)
                    isNearPosition(MemoryTypes.POINT_OF_INTEREST)
                    lambda {
                        tick {
                            val position = ai.getMemory(MemoryTypes.POINT_OF_INTEREST)
                                ?: return@tick TreeNode.InvocationResult.FAIL
                            farm(position.pos)
                        }
                    }
                    setOccupied(MemoryTypes.POINT_OF_INTEREST, false)
                }

                failer {
                    and {
                        setOccupied(MemoryTypes.POINT_OF_INTEREST, false)
                    }
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

    private fun farm(position: BlockPos): TreeNode.InvocationResult {
        val blockPos = position.up()

        val world = world as ServerWorld
        val state = world.getBlockState(blockPos)
        val block = state.block

        if (state.isAir) {
            world.setBlockState(blockPos, ChorusWheatBlock.BLOCK.defaultState, 3)
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

            return TreeNode.InvocationResult.SUCCESS
        }

        if (block is ChorusWheatBlock && block.isMature(state)) {
            world.breakBlock(blockPos, false, this@FarmerEnderman)
            crops++
            return TreeNode.InvocationResult.SUCCESS
        }

        return TreeNode.InvocationResult.FAIL
    }
}
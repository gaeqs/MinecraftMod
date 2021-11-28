package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.findPointOfInterest
import io.github.gaeqs.magicend.ai.defaults.tree.walkToPosition
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*
import io.github.gaeqs.magicend.block.entity.EnderBreadPlateBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.Identifier
import net.minecraft.world.World

class FarmerEnderman(type: EntityType<out FarmerEnderman>, world: World) : EnderVillager(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "farmer_enderman")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<FarmerEnderman> { type, world -> FarmerEnderman(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 3.0f)).build()

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
        }
    }

    init {
        initAI()
    }

    private fun initAI() {
        ai.activities += TreeActivity("idle", ai, rootLoopUnconditional {
            or {
                and {
                    findPointOfInterest(PointOfInterestTypes.ENDER_BREAD_PLATE, MemoryTypes.POINT_OF_INTEREST, 48) {
                        val entity = world.getBlockEntity(it)
                        entity is EnderBreadPlateBlockEntity && !entity.isFull()
                    }
                    walkToPosition(MemoryTypes.POINT_OF_INTEREST, 2.0f)
                    // Position reached successfully. Waiting.
                    wait(20)
                    lambda {
                        tick {
                            val pos = ai.getMemory(MemoryTypes.POINT_OF_INTEREST)
                                ?: return@tick TreeNode.InvocationResult.FAIL

                            val entity = world.getBlockEntity(pos.pos)
                            if (entity !is EnderBreadPlateBlockEntity || entity.isFull())
                                return@tick TreeNode.InvocationResult.FAIL

                            entity.amount++

                            TreeNode.InvocationResult.SUCCESS
                        }
                    }
                    wait(50)
                }
                and {
                    // Walk failed! Just in case skip a tick.
                    wait(10)
                }
            }
        })

    }

}
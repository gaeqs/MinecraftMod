package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.ai.defaults.canNavigateToEntity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemGroup
import net.minecraft.item.Items
import net.minecraft.item.SpawnEggItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.world.World

class ShamanEnderman(type: EntityType<out ShamanEnderman>, world: World) : EnderVillager(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "shaman_enderman")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<ShamanEnderman> { type, world -> ShamanEnderman(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 3.0f)).build()

        val EGG_ITEM_IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "shaman_enderman_spawn_egg")
        val EGG_ITEM = SpawnEggItem(
            ENTITY_TYPE, 0x161616, 0x5e005a,
            FabricItemSettings().group(ItemGroup.MISC)
        )

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
        }
    }

    var sacrifices: Int = 0

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        sacrifices = nbt.getInt("sacrifices")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("sacrifices", sacrifices)
    }

    override fun TreeNodeParentBuilder<*>.initWorkAI() {

        // SACRIFICES
        and {
            predicate { sacrifices >= 5 }
            findPointOfInterest(PointOfInterestTypes.DRAGON_STATUE, MemoryTypes.POINT_OF_INTEREST, 64)
            walkToPosition(MemoryTypes.POINT_OF_INTEREST, 1.5f)
            wait(50)
            isNearPosition(MemoryTypes.POINT_OF_INTEREST)
            predicate { sacrifices >= 5 }
            lambda {
                tick {
                    val pos = ai.getMemory(MemoryTypes.POINT_OF_INTEREST) ?: return@tick TreeNode.InvocationResult.FAIL
                    sacrifices -= 5
                    village.executeRitual(this@ShamanEnderman, pos.pos)
                    TreeNode.InvocationResult.SUCCESS
                }
            }
        }

        and {
            findNearestItemEntities(48.0)
            findEntityIfNotFound(
                MemoryTypes.NEARBY_ITEM_ENTITIES,
                MemoryTypes.TARGET_ITEM,
                48.0f
            ) { it.stack.item == Items.ITEM_FRAME && canNavigateToEntity(it) }

            walkToEntity(MemoryTypes.TARGET_ITEM, 1.5f, 1.0f, 48.0f)
            isEntityTargetValid(MemoryTypes.TARGET_ITEM, 48.0f)
            isNearEntity(MemoryTypes.TARGET_ITEM, 1.5f)
            lambda {
                tick {
                    val item = ai.getMemory(MemoryTypes.TARGET_ITEM) ?: return@tick TreeNode.InvocationResult.FAIL
                    sacrifices += item.stack.count
                    ai.getMemory(MemoryTypes.TARGET_ITEM)?.remove()
                    TreeNode.InvocationResult.SUCCESS
                }
            }
        }

        and {
            succeeder {
                and {
                    findRandomWalkTargetToPoint(1.5f, PointOfInterestTypes.DRAGON_STATUE)
                    timed(60, 100) {
                        walkToTarget()
                    }
                }
            }
            wait(20)
        }
    }
}

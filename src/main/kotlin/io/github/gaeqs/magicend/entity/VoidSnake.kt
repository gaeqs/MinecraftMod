package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.SpawnEggItem
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import java.util.*

class VoidSnake(type: EntityType<out VoidSnake>, world: World) : AIEntity(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_snake")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.MONSTER,
            EntityType.EntityFactory(::VoidSnake)
        ).dimensions(EntityDimensions.fixed(1.5f, 0.8f)).build()

        val EGG_ITEM_IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_snake_spawn_egg")
        val EGG_ITEM = SpawnEggItem(
            ENTITY_TYPE, 0x946794, 0x140054,
            FabricItemSettings().group(ItemGroup.MISC)
        )

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
        }

        fun canSpawn(world: WorldAccess, spawnReason: SpawnReason, pos: BlockPos, random: Random): Boolean {
            return canMobSpawn(ENTITY_TYPE, world, spawnReason, pos, random) && world.difficulty != Difficulty.PEACEFUL
        }

    }

    init {
        initAI()
    }

    override fun isDisallowedInPeaceful() = true

    private fun initAI() {
        ai.activities += TreeActivity("default", ai, rootLoopUnconditional {
            runAndWait {
                or {
                    and {
                        findNearestLivingEntities()
                        or {
                            findAttackTargetIfNotFound(32.0f) { it is EnderVillager }
                            findAttackTargetIfNotFound(32.0f) { it is PlayerEntity && !it.isCreative && !it.isSpectator }
                        }
                        walkToEntity(MemoryTypes.ATTACK_TARGET, 1.5f, 1.0f, 32.0f)
                        succeeder {
                            attack()
                        }
                    }
                    and {
                        findRandomWalkTarget(1.0f)
                        timed(40, 60) {
                            walkToTarget()
                        }
                    }
                }
            }
        })
    }

}
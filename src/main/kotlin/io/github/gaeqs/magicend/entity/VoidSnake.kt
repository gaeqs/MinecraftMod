package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.Identifier
import net.minecraft.world.World

class VoidSnake(type: EntityType<out VoidSnake>, world: World) : AIEntity(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_snake")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<VoidSnake> { type, world -> VoidSnake(type, world) }
        ).dimensions(EntityDimensions.fixed(1.5f, 0.8f)).build()

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
        }
    }

    init {
        initAI()
    }

    private fun initAI() {
        ai.activities += TreeActivity("default", ai, rootLoopUnconditional {
            runAndWait {
                or {
                    and {
                        findNearestLivingEntities()
                        findAttackTargetIfNotFound(32.0f) { it is EnderVillager }
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
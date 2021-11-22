package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.random.Random

class GuardianEnderman(type: EntityType<out GuardianEnderman>, world: World) : EnderVillager(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "guardian_enderman")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<GuardianEnderman> { type, world -> GuardianEnderman(type, world) }
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
            and {
                findWalkTarget(1.0f)
                findNearestLivingEntities()
                succeeder {
                    simultaneously {
                        timed(50, 100) {
                            lookAtNearestLivingEntity()
                        }
                        timed(50, 200) {
                            walkToTarget()
                        }
                    }
                }
                wait(100)
            }
        })

    }

}
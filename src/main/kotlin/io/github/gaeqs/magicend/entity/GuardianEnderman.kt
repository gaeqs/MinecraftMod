package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.World

class GuardianEnderman(type: EntityType<out GuardianEnderman>, world: World) : EnderVillager(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "guardian_enderman")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<GuardianEnderman> { type, world -> GuardianEnderman(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 3.0f)).build()

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5)
        }
    }

    var patrolling = false
    var lastTimePatrolling: Long = 0
    var kills: Int = 0

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        kills = nbt.getInt("kills")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("kills", kills)
    }


    override fun onKilledOther(world: ServerWorld?, other: LivingEntity?) {
        super.onKilledOther(world, other)
        kills++
    }

    override fun addRunAwayActivity() {
        ai.activities += TreeActivity(EnderVillagerStatus.RUNNING_AWAY.activityName, ai, rootLoopUnconditional {
            or {
                and {
                    succeeder {
                        findAttackTargetIfNotFound(32.0f)
                        { it is VoidSnake || it is VoidWorm || it is VoidSquid }
                    }
                    walkToEntity(MemoryTypes.ATTACK_TARGET, 1.5f, 1.0f, 32.0f)
                    isEntityTargetValid(MemoryTypes.ATTACK_TARGET, 32.0f)
                    isNearEntity(MemoryTypes.ATTACK_TARGET, 1.5f)
                    succeeder {
                        attack()
                    }
                    wait(1)
                }
                wait(10)
            }
        })
    }

    override fun TreeNodeParentBuilder<*>.initWorkAI() {

        // Check if anybody needs help
        and {
            findEnderVillager(MemoryTypes.HELPING_VILLAGER) { it.status == EnderVillagerStatus.RUNNING_AWAY }
            walkToEntity(MemoryTypes.HELPING_VILLAGER, 2.5f, 5.0f, 32.0f)
            wait(1)
        }

        and {
            predicate { patrolling }
            predicate { kills > 5 && village.guardians.any { it != this@GuardianEnderman } }
            lambda {
                tick {
                    // End patrolling
                    kills = 0
                    patrolling = false
                    lastTimePatrolling = System.currentTimeMillis()
                    village.callToPatrol()

                    // This fail assures the next ands will be called.
                    TreeNode.InvocationResult.FAIL
                }
            }
        }

        and {
            predicate { patrolling }
            findRandomWalkTarget(1.5f)
            timed(60, 100) {
                walkToTarget()
            }
            wait(5)
        }

        and {
            predicate { !patrolling }
            findPointOfInterest(PointOfInterestTypes.ENDER_TABLE, MemoryTypes.POINT_OF_INTEREST, 64)
            walkToPosition(MemoryTypes.POINT_OF_INTEREST, 1.5f)
            wait(20)
        }

        wait(20)
    }
}
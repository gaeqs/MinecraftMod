package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
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
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.World

class VoidWorm(type: EntityType<out VoidWorm>, world: World) : AIEntity(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_worm")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<VoidWorm> { type, world -> VoidWorm(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 0.8f)).build()

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
        }
    }

    var kills: Int = 0

    init {
        initAI()
    }

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
        if (kills >= 5) {
            evolve()
        }
    }


    private fun initAI() {
        ai.activities += TreeActivity("default", ai, rootLoopUnconditional {
            runAndWait {
                or {
                    and {
                        findNearestLivingEntities()
                        findAttackTargetIfNotFound { it is EnderVillager }
                        walkToEntity(MemoryTypes.ATTACK_TARGET, 1.5f, 2.0f)
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

    private fun evolve() {
        if (removed) return
        method_29243(VoidSnake.ENTITY_TYPE, true)
    }

}
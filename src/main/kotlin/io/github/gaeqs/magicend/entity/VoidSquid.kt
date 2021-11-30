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
import net.minecraft.entity.ai.control.FlightMoveControl
import net.minecraft.entity.ai.pathing.BirdNavigation
import net.minecraft.entity.ai.pathing.EntityNavigation
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class VoidSquid(type: EntityType<out VoidSquid>, world: World) : AIEntity(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_squid")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<VoidSquid> { type, world -> VoidSquid(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 2.0f)).build()

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
        }
    }

    var shamanKills: Int = 0

    init {
        moveControl = FlightMoveControl(this, 20, true)
        initAI()
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        shamanKills = nbt.getInt("shaman_kills")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("shaman_kills", shamanKills)
    }

    override fun onKilledOther(world: ServerWorld?, other: LivingEntity?) {
        super.onKilledOther(world, other)
        if (other is ShamanEnderman) shamanKills++
    }

    override fun createNavigation(world: World?): EntityNavigation {
        val birdNavigation: BirdNavigation = object : BirdNavigation(this, world) {
            override fun isValidPosition(pos: BlockPos): Boolean {
                return !this.world.getBlockState(pos.down()).isAir
            }
        }
        birdNavigation.setCanPathThroughDoors(false)
        birdNavigation.setCanSwim(false)
        birdNavigation.setCanEnterOpenDoors(true)
        return birdNavigation
    }

    override fun handleFallDamage(fallDistance: Float, damageMultiplier: Float) = false

    private fun initAI() {
        ai.activities += TreeActivity("default", ai, rootLoopUnconditional {
            runAndWait {
                or {

                    and {
                        predicate { shamanKills > 0 && health < maxHealth }
                        lambda {
                            tick {
                                addStatusEffect(
                                    StatusEffectInstance(
                                        StatusEffects.REGENERATION,
                                        shamanKills * 40,
                                        0
                                    )
                                )
                                shamanKills = 0
                                TreeNode.InvocationResult.SUCCESS
                            }
                        }
                    }

                    and {
                        findNearestLivingEntities()
                        findAttackTargetIfNotFound { it is ShamanEnderman }
                        walkToEntity(MemoryTypes.ATTACK_TARGET, 1.5f, 2.0f)
                        succeeder {
                            attack()
                        }
                    }

                    and {
                        findAttackTargetIfNotFound { it is EnderVillager }
                        walkToEntity(MemoryTypes.ATTACK_TARGET, 1.5f, 2.0f)
                        succeeder {
                            attack()
                        }
                    }

                    and {
                        findRandomWalkTarget(1.0f, air = true)
                        timed(40, 60) {
                            walkToTarget()
                        }
                    }
                }
            }
        })
    }

}
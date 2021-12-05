package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.*
import io.github.gaeqs.magicend.block.entity.EnderBreadPlateBlockEntity
import io.github.gaeqs.magicend.village.EndVillage
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.particle.ParticleTypes
import net.minecraft.world.World

open class EnderVillager(
    type: EntityType<out EnderVillager>,
    world: World
) : AIEntity(type, world) {

    var status = EnderVillagerStatus.NORMAL
        private set

    var village: EndVillage
        private set

    init {
        village = EndVillage().apply { add(this@EnderVillager) }
        initAI()
    }

    protected open fun changeStatus(status: EnderVillagerStatus) {
        if (status == this.status) return
        this.status = status
        val values = EnderVillagerStatus.values().map { it.activityName }
        ai.activities.removeIf { it.name in values }
        when (status) {
            EnderVillagerStatus.RUNNING_AWAY -> addRunAwayActivity()
            EnderVillagerStatus.NORMAL -> addNormalActivity()
        }
    }

    protected open fun addRunAwayActivity() {
        ai.activities += TreeActivity(EnderVillagerStatus.RUNNING_AWAY.activityName, ai, rootLoopUnconditional {
            and {
                or {
                    and {
                        findPointOfInterest(PointOfInterestTypes.ENDER_TABLE, MemoryTypes.POINT_OF_INTEREST, 64)
                        walkToPosition(MemoryTypes.POINT_OF_INTEREST, 2.5f)
                        wait(20)
                    }
                    and {
                        findRandomWalkTarget(2.5f)
                        timed(40, 70) {
                            walkToTarget()
                        }
                    }

                    succeeder {}
                }
                wait(1)
            }
        })
    }

    protected open fun addNormalActivity() {
        ai.activities += TreeActivity(EnderVillagerStatus.NORMAL.activityName, ai, rootLoopUnconditional {
            or {
                // EAT BREAD
                and {
                    predicate { health < maxHealth && !hasStatusEffect(StatusEffects.REGENERATION) }
                    findPointOfInterest(PointOfInterestTypes.ENDER_BREAD_PLATE, MemoryTypes.POINT_OF_INTEREST, 48) {
                        val entity = world.getBlockEntity(it)
                        entity is EnderBreadPlateBlockEntity && !entity.isEmpty()
                    }
                    walkToPosition(MemoryTypes.POINT_OF_INTEREST, 1.5f)
                    // Position reached successfully. Waiting.
                    wait(50)
                    isNearPosition(MemoryTypes.POINT_OF_INTEREST)
                    succeeder {
                        lambda {
                            tick {
                                val pos = ai.getMemory(MemoryTypes.POINT_OF_INTEREST)
                                    ?: return@tick TreeNode.InvocationResult.FAIL
                                val entity = world.getBlockEntity(pos.pos)
                                if (entity !is EnderBreadPlateBlockEntity || entity.isEmpty()) return@tick TreeNode.InvocationResult.FAIL
                                entity.amount--

                                addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 40, 1))
                                TreeNode.InvocationResult.SUCCESS
                            }
                        }
                    }
                }

                // WORK
                initWorkAI()
            }
        })
    }


    protected open fun TreeNodeParentBuilder<*>.initWorkAI() {
        wait(10)
    }

    override fun remove() {
        super.remove()
        village.refresh()
    }

    override fun tickMovement() {
        if (world.isClient) {
            repeat(2) {
                world.addParticle(
                    ParticleTypes.PORTAL,
                    getParticleX(0.5),
                    this.randomBodyY - 0.25,
                    getParticleZ(0.5),
                    (random.nextDouble() - 0.5) * 2.0,
                    -random.nextDouble(),
                    (random.nextDouble() - 0.5) * 2.0
                )
            }
        }
        if (village.villagers.size == 1 && random.nextDouble() > 0.8 || random.nextDouble() > 0.99) {
            searchBiggerVillages()
        }

        super.tickMovement()
    }

    private fun searchBiggerVillages() {
        val box = boundingBox.expand(64.0, 32.0, 64.0)
        val other = world.getEntitiesByClass(EnderVillager::class.java, box) {
            it != this && it.isAlive && it.village != village && it.village.villagers.size >= village.villagers.size
        }.maxByOrNull { it.village.villagers.size } ?: return

        village.remove(this)
        village = other.village
        village.add(this)
    }

    private fun initAI() {
        ai.activities += TreeActivity("activity_manager", ai, rootLoopUnconditional {
            and {
                or {
                    and {
                        findNearestLivingEntities()
                        isEntityNear { it is VoidSnake || it is VoidWorm || it is VoidSquid }
                        lambda {
                            tick {
                                changeStatus(EnderVillagerStatus.RUNNING_AWAY)
                                TreeNode.InvocationResult.SUCCESS
                            }
                        }
                    }

                    lambda {
                        tick {
                            changeStatus(EnderVillagerStatus.NORMAL)
                            TreeNode.InvocationResult.SUCCESS
                        }
                    }
                }

                wait(20)
            }
        })
        addNormalActivity()
    }
}

enum class EnderVillagerStatus(val activityName: String) {
    RUNNING_AWAY("running_away"),
    NORMAL("normal");
}
package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.*
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.dynamic.GlobalPos


fun TreeNodeParentBuilder<*>.walkToPosition(memory: MemoryType<GlobalPos>, speed: Float) {
    and {
        loopUntilFail {
            and {
                // If it's already in the position, return
                inverter {
                    isNearPosition(memory)
                }

                // Find the path.
                findPositionWalkTarget(memory, speed)

                // We need the timer because the walk target pathfinder works like sh** and needs to reload.
                // Thx Minecraft. :)
                timed(100, 150) {
                    // Walk a little.
                    walkToTarget()
                }
            }
        }
        isNearPosition(memory)
    }

}

fun TreeNodeParentBuilder<*>.walkToEntity(
    memory: MemoryType<out Entity>,
    speed: Float,
    minDistance: Float,
    distance: Float
) {
    and {
        loopUntilFail {
            and {
                // If it's already in the position, return
                isEntityTargetValid(memory, distance)
                inverter {
                    isNearEntity(memory, minDistance)
                }

                // Find the path.
                findEntityWalkTarget(memory, speed, 0)

                // We need the timer because the walk target pathfinder works like sh** and needs to reload.
                // Thx Minecraft. :)
                timed(10, 20) {
                    // Walk a little.
                    walkToTarget()
                }
            }
        }
        isEntityTargetValid(memory, distance)
        isNearEntity(memory, minDistance)
    }
}

fun TreeNodeParentBuilder<*>.runAndWait(builder: TreeNodeSucceeder.Builder.() -> Unit) {
    and {
        succeeder {
            builder()
        }
        wait(1)
    }
}

fun <T : Entity> TreeNodeParentBuilder<*>.findEntityIfNotFound(
    list: MemoryType<out Collection<T>>,
    saveOn: MemoryType<T>,
    distance: Float,
    condition: (T) -> Boolean
) {
    and {
        succeeder {
            and {
                inverter {
                    isEntityTargetValid(saveOn, distance)
                }
                findEntity(list, saveOn, condition)
            }
        }
        isEntityTargetValid(saveOn, distance)
    }
}

fun TreeNodeParentBuilder<*>.findAttackTargetIfNotFound(distance: Float, condition: (LivingEntity) -> Boolean) {
    and {
        succeeder {
            and {
                inverter {
                    isEntityTargetValid(MemoryTypes.ATTACK_TARGET, distance)
                }
                findEntity(condition)
            }
        }
        isEntityTargetValid(MemoryTypes.ATTACK_TARGET, distance)
    }
}
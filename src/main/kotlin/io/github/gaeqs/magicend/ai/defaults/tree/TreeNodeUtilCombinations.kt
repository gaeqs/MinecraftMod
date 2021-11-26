package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.and
import io.github.gaeqs.magicend.ai.tree.node.inverter
import io.github.gaeqs.magicend.ai.tree.node.loopUntilFail
import io.github.gaeqs.magicend.ai.tree.node.timed
import net.minecraft.entity.LivingEntity
import net.minecraft.util.dynamic.GlobalPos
import kotlin.math.floor


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
                timed(10, 20) {
                    // Walk a little.
                    walkToTarget()
                }
            }
        }
        isNearPosition(memory)
    }

}

fun TreeNodeParentBuilder<*>.walkToEntity(memory: MemoryType<out LivingEntity>, speed: Float, minDistance: Float) {
    and {
        loopUntilFail {
            and {
                // If it's already in the position, return
                isAttackTargetValid(memory)
                inverter {
                    isNearEntity(memory, minDistance)
                }

                // Find the path.
                findEntityWalkTarget(memory, speed, floor(minDistance).toInt())

                // We need the timer because the walk target pathfinder works like sh** and needs to reload.
                // Thx Minecraft. :)
                timed(10, 20) {
                    // Walk a little.
                    walkToTarget()
                }
            }
        }
        isNearEntity(memory, minDistance)
    }

}
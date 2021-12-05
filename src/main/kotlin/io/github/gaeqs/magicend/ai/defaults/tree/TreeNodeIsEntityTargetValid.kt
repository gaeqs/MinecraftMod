package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity

class TreeNodeIsTargetEntityValid(activity: Activity, val memory: MemoryType<out Entity>, val distanceSq: Float) :
    TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val memory = ai.getMemory(memory) ?: return InvocationResult.FAIL

        if (memory.removed || (memory is LivingEntity && memory.isDead) || memory.squaredDistanceTo(entity) > distanceSq
            || memory.world.registryKey != entity.world.registryKey
        ) {
            return InvocationResult.FAIL
        }

        return InvocationResult.SUCCESS
    }

    override fun stop() {
    }


    class Builder(var memory: MemoryType<out Entity>, var distance: Float) :
        TreeNodeBuilder<TreeNodeIsTargetEntityValid> {
        override fun build(activity: Activity) = TreeNodeIsTargetEntityValid(activity, memory, distance * distance)
    }
}

fun TreeNodeParentBuilder<*>.isEntityTargetValid(
    memory: MemoryType<out Entity>, distance: Float
) = addChild(TreeNodeIsTargetEntityValid.Builder(memory, distance))

package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.Entity

class TreeNodeIsNearEntity(
    activity: Activity,
    val memory: MemoryType<out Entity>,
    val distance: Float
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val target = ai.getMemory(memory) ?: return InvocationResult.FAIL
        if (entity.boundingBox.expand(distance.toDouble()).intersects(target.boundingBox))
            return InvocationResult.SUCCESS
        return InvocationResult.FAIL
    }

    class Builder(
        var memory: MemoryType<out Entity>,
        var distance: Float
    ) : TreeNodeBuilder<TreeNodeIsNearEntity> {
        override fun build(activity: Activity) = TreeNodeIsNearEntity(activity, memory, distance)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.isNearEntity(memory: MemoryType<out Entity>, distance: Float) =
    addChild(TreeNodeIsNearEntity.Builder(memory, distance))
package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.util.dynamic.GlobalPos

class TreeNodeIsNearPosition(
    activity: Activity,
    val memory: MemoryType<GlobalPos>
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val position = ai.getMemory(memory) ?: return InvocationResult.FAIL
        if (entity.blockPos.getSquaredDistance(position.pos) < 8.0) return InvocationResult.SUCCESS
        return InvocationResult.FAIL
    }

    class Builder(
        var memory: MemoryType<GlobalPos>,
    ) : TreeNodeBuilder<TreeNodeIsNearPosition> {
        override fun build(activity: Activity) = TreeNodeIsNearPosition(activity, memory)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.isNearPosition(memory: MemoryType<GlobalPos>) =
    addChild(TreeNodeIsNearPosition.Builder(memory))
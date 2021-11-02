package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopN(val child: TreeNode, val times: Int) : TreeNode() {

    private var executed = 0

    override fun reset() = child.reset()

    override fun invoke(): InvocationResult {
        while (executed < times) {
            if (child() == InvocationResult.WAIT) return InvocationResult.WAIT
            executed++
            if (executed < times) {
                child.reset()
            }
        }
        return InvocationResult.SUCCESS
    }

    class Builder(var times: Int) : TreeNodeUniqueParentBuilder<TreeNodeLoopN>() {
        override fun build() = TreeNodeLoopN(child.build(), times)
    }
}

inline fun TreeNodeParentBuilder<*>.loopN(times: Int, builder: TreeNodeLoopN.Builder.() -> Unit) {
    val b = TreeNodeLoopN.Builder(times)
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.loopN(times: Int, builder: TreeNodeLoopN.Builder.() -> Unit) {
    val b = TreeNodeLoopN.Builder(times)
    child = b
    builder(b)
}
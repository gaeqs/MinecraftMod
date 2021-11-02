package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopUntilFail(val child: TreeNode) : TreeNode() {

    override fun reset() = child.reset()

    override fun invoke(): InvocationResult {
        while (true) {
            when (child()) {
                InvocationResult.WAIT -> return InvocationResult.WAIT
                InvocationResult.FAIL -> return InvocationResult.FAIL
                else -> child.reset()
            }
        }
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeLoopUntilFail>() {
        override fun build() = TreeNodeLoopUntilFail(child.build())
    }
}

inline fun TreeNodeParentBuilder<*>.loopUntilFail(builder: TreeNodeLoopUntilFail.Builder.() -> Unit) {
    val b = TreeNodeLoopUntilFail.Builder()
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.loopUntilFail(builder: TreeNodeLoopUntilFail.Builder.() -> Unit) {
    val b = TreeNodeLoopUntilFail.Builder()
    child = b
    builder(b)
}
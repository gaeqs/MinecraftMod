package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopUntilSuccess(val child: TreeNode) : TreeNode() {

    override fun reset() = child.reset()

    override fun invoke(): InvocationResult {
        while (true) {
            when (child()) {
                InvocationResult.WAIT -> return InvocationResult.WAIT
                InvocationResult.SUCCESS -> return InvocationResult.SUCCESS
                else -> child.reset()
            }
        }
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeLoopUntilSuccess>() {
        override fun build() = TreeNodeLoopUntilSuccess(child.build())
    }
}

inline fun TreeNodeParentBuilder<*>.loopUntilSucceed(builder: TreeNodeLoopUntilSuccess.Builder.() -> Unit) {
    val b = TreeNodeLoopUntilSuccess.Builder()
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.loopUntilSucceed(builder: TreeNodeLoopUntilSuccess.Builder.() -> Unit) {
    val b = TreeNodeLoopUntilSuccess.Builder()
    child = b
    builder(b)
}
package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopUntilSuccess(activity: Activity, val child: TreeNode) : TreeNode(activity) {

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
        override fun build(activity: Activity) = TreeNodeLoopUntilSuccess(activity, child.build(activity))
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

inline fun loopUntilSucceed(builder: TreeNodeLoopUntilSuccess.Builder.() -> Unit): TreeNodeLoopUntilSuccess.Builder {
    return TreeNodeLoopUntilSuccess.Builder().also { builder(it) }
}
package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopUntilFail(activity: Activity, val child: TreeNode) : TreeNode(activity) {

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
        override fun build(activity: Activity) = TreeNodeLoopUntilFail(activity, child.build(activity))
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

inline fun loopUntilFail(builder: TreeNodeLoopUntilFail.Builder.() -> Unit): TreeNodeLoopUntilFail.Builder {
    return TreeNodeLoopUntilFail.Builder().also { builder(it) }
}
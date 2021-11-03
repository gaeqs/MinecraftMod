package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopUnconditional(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun reset() = child.reset()

    override fun invoke(): InvocationResult {
        while (child() != InvocationResult.WAIT) {
            child.reset()
        }
        return InvocationResult.WAIT
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeLoopUnconditional>() {
        override fun build(activity: Activity) = TreeNodeLoopUnconditional(activity, child.build(activity))
    }
}

inline fun TreeNodeParentBuilder<*>.loopUnconditional(builder: TreeNodeLoopUnconditional.Builder.() -> Unit) {
    val b = TreeNodeLoopUnconditional.Builder()
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.loopUnconditional(builder: TreeNodeLoopUnconditional.Builder.() -> Unit) {
    val b = TreeNodeLoopUnconditional.Builder()
    child = b
    builder(b)
}

inline fun loopUnconditional(builder: TreeNodeLoopUnconditional.Builder.() -> Unit): TreeNodeLoopUnconditional.Builder {
    return TreeNodeLoopUnconditional.Builder().also { builder(it) }
}
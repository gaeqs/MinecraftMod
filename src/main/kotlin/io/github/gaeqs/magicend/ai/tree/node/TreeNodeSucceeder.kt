package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeSucceeder(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun reset() = child.reset()

    override fun invoke(): InvocationResult {
        child()
        return InvocationResult.SUCCESS
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeSucceeder>() {
        override fun build(activity: Activity) = TreeNodeSucceeder(activity, child.build(activity))
    }
}

inline fun TreeNodeParentBuilder<*>.succeeder(builder: TreeNodeSucceeder.Builder.() -> Unit) {
    val b = TreeNodeSucceeder.Builder()
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.succeeder(builder: TreeNodeSucceeder.Builder.() -> Unit) {
    val b = TreeNodeSucceeder.Builder()
    child = b
    builder(b)
}
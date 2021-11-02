package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeSucceeder(val child: TreeNode) : TreeNode() {

    override fun reset() = child.reset()

    override fun invoke(): InvocationResult {
        child()
        return InvocationResult.SUCCESS
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeSucceeder>() {
        override fun build() = TreeNodeSucceeder(child.build())
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
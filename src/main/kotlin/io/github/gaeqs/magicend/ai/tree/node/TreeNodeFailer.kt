package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeFailer(val child: TreeNode) : TreeNode() {

    override fun reset() = child.reset()

    override fun invoke(): InvocationResult {
        child()
        return InvocationResult.FAIL
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeFailer>() {
        override fun build() = TreeNodeFailer(child.build())
    }
}

inline fun TreeNodeParentBuilder<*>.failer(builder: TreeNodeFailer.Builder.() -> Unit) {
    val b = TreeNodeFailer.Builder()
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.failer(builder: TreeNodeFailer.Builder.() -> Unit) {
    val b = TreeNodeFailer.Builder()
    child = b
    builder(b)
}
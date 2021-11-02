package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeInverter(val child: TreeNode) : TreeNode() {

    override fun reset() = child.reset()
    override fun invoke() = child().not()

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeInverter>() {
        override fun build() = TreeNodeInverter(child.build())
    }
}

inline fun TreeNodeParentBuilder<*>.inverter(builder: TreeNodeInverter.Builder.() -> Unit) {
    val b = TreeNodeInverter.Builder()
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.inverter(builder: TreeNodeInverter.Builder.() -> Unit) {
    val b = TreeNodeInverter.Builder()
    child = b
    builder(b)
}
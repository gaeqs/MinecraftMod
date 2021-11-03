package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeInverter(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun invoke() = child().not()

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeInverter>() {
        override fun build(activity: Activity) = TreeNodeInverter(activity, child.build(activity))
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
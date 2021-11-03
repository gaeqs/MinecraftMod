package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeFailer(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        if (child.tick() == InvocationResult.WAIT) return InvocationResult.WAIT
        return InvocationResult.FAIL
    }

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeFailer>() {
        override fun build(activity: Activity) = TreeNodeFailer(activity, child.build(activity))
    }
}

inline fun TreeNodeParentBuilder<*>.failer(builder: TreeNodeFailer.Builder.() -> Unit) = TreeNodeFailer.Builder().also {
    addChild(it)
    builder(it)
}
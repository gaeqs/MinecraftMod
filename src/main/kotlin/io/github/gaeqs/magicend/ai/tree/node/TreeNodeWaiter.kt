package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeWaiter(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        if (child.tick() != InvocationResult.WAIT) {
            child.stop()
            child.start()
        }
        return InvocationResult.WAIT
    }

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeWaiter>() {
        override fun build(activity: Activity) = TreeNodeWaiter(activity, child.build(activity))
    }
}

inline fun TreeNodeParentBuilder<*>.waiter(builder: TreeNodeWaiter.Builder.() -> Unit) =
    TreeNodeWaiter.Builder().also {
        addChild(it)
        builder(it)
    }
package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopUntilSuccess(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        while (true) {
            when (child.tick()) {
                InvocationResult.WAIT -> return InvocationResult.WAIT
                InvocationResult.SUCCESS -> return InvocationResult.SUCCESS
                else -> {
                    child.stop()
                    child.start()
                }
            }
        }
    }

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeLoopUntilSuccess>() {
        override fun build(activity: Activity) = TreeNodeLoopUntilSuccess(activity, child.build(activity))
    }
}

inline fun TreeNodeParentBuilder<*>.loopUntilSuccess(builder: TreeNodeLoopUntilSuccess.Builder.() -> Unit) =
    TreeNodeLoopUntilSuccess.Builder().also {
        addChild(it)
        builder(it)
    }

inline fun loopUntilSucceed(builder: TreeNodeLoopUntilSuccess.Builder.() -> Unit): TreeNodeLoopUntilSuccess.Builder {
    return TreeNodeLoopUntilSuccess.Builder().also { builder(it) }
}
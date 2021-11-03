package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopUntilFail(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun invoke(): InvocationResult {
        while (true) {
            when (child()) {
                InvocationResult.WAIT -> return InvocationResult.WAIT
                InvocationResult.FAIL -> return InvocationResult.FAIL
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

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeLoopUntilFail>() {
        override fun build(activity: Activity) = TreeNodeLoopUntilFail(activity, child.build(activity))
    }
}

inline fun TreeNodeParentBuilder<*>.loopUntilFail(builder: TreeNodeLoopUntilFail.Builder.() -> Unit) =
    TreeNodeLoopUntilFail.Builder().also {
        addChild(it)
        builder(it)
    }

inline fun loopUntilFail(builder: TreeNodeLoopUntilFail.Builder.() -> Unit): TreeNodeLoopUntilFail.Builder {
    return TreeNodeLoopUntilFail.Builder().also { builder(it) }
}
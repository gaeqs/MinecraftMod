package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

class TreeNodeLambda(
    activity: Activity,
    val start: TreeNode.() -> Unit,
    val tick: TreeNode.() -> InvocationResult,
    val stop: TreeNode.() -> Unit,
) : TreeNode(activity) {

    override fun tick() = tick(this)
    override fun start() = start(this)
    override fun stop() = stop(this)

    class Builder(
        var start: TreeNode.() -> Unit = { },
        var tick: TreeNode.() -> InvocationResult = { InvocationResult.FAIL },
        var stop: TreeNode.() -> Unit = { },
    ) : TreeNodeBuilder<TreeNodeLambda> {
        override fun build(activity: Activity) = TreeNodeLambda(activity, start, tick, stop)

        fun start(start: TreeNode.() -> Unit) {
            this.start = start
        }

        fun tick(tick: TreeNode.() -> InvocationResult) {
            this.tick = tick
        }

        fun stop(stop: TreeNode.() -> Unit) {
            this.stop = stop
        }

    }
}

inline fun TreeNodeParentBuilder<*>.lambda(builder: TreeNodeLambda.Builder.() -> Unit) =
    TreeNodeLambda.Builder().also {
        addChild(it)
        builder(it)
    }
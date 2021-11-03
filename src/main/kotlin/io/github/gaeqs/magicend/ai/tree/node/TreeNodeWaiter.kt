package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

class TreeNodeWaiter(activity: Activity, val ticks: Int, val result: InvocationResult = InvocationResult.SUCCESS) :
    TreeNode(activity) {

    private var current = 0

    override fun tick(): InvocationResult {
        if (current < ticks) {
            current++
            return InvocationResult.WAIT
        }
        return result
    }

    override fun start() {
        current = 0
    }

    override fun stop() {
    }

    class Builder(var ticks: Int) : TreeNodeBuilder<TreeNodeWaiter> {
        override fun build(activity: Activity) = TreeNodeWaiter(activity, ticks)
    }
}

fun TreeNodeParentBuilder<*>.wait(times: Int) = addChild(TreeNodeWaiter.Builder(times))
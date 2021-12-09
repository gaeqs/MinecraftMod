package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

class TreeNodeWait(activity: Activity, val ticks: Int, val result: InvocationResult = InvocationResult.SUCCESS) :
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

    class Builder(var ticks: Int) : TreeNodeBuilder<TreeNodeWait> {
        override fun build(activity: Activity) = TreeNodeWait(activity, ticks)
    }
}

fun TreeNodeParentBuilder<*>.wait(ticks: Int) = addChild(TreeNodeWait.Builder(ticks))
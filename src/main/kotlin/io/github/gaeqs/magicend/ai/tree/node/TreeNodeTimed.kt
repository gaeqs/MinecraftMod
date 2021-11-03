package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder
import kotlin.random.Random

class TreeNodeTimed(activity: Activity, val minTicks: Int, val maxTicks: Int, val child: TreeNode) :
    TreeNode(activity) {

    private var current = 0
    private var times: Int = Random.Default.nextInt(minTicks, maxTicks)
    private var result: InvocationResult? = null

    override fun start() {
        current = 0
        times = Random.Default.nextInt(minTicks, maxTicks)
        result = null
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    override fun invoke(): InvocationResult {
        result?.let { return it }
        if (current < times) {

            val childResult = child()
            if (childResult != InvocationResult.WAIT) {
                result = childResult
                return childResult
            }

            current++
            return InvocationResult.WAIT
        }

        result = InvocationResult.SUCCESS
        return InvocationResult.SUCCESS
    }

    class Builder(var minTicks: Int, var maxTicks: Int) : TreeNodeUniqueParentBuilder<TreeNodeTimed>() {
        override fun build(activity: Activity) = TreeNodeTimed(activity, minTicks, maxTicks, child.build(activity))
    }
}

fun TreeNodeParentBuilder<*>.timed(minTicks: Int, maxTicks: Int, builder: TreeNodeTimed.Builder.() -> Unit) {
    val b = TreeNodeTimed.Builder(minTicks, maxTicks)
    children.add(b)
    builder(b)
}

fun TreeNodeUniqueParentBuilder<*>.timed(minTicks: Int, maxTicks: Int, builder: TreeNodeTimed.Builder.() -> Unit) {
    val b = TreeNodeTimed.Builder(minTicks, maxTicks)
    child = b
    builder(b)
}
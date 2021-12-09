package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder
import kotlin.random.Random

/**
 * A node that will execute its child node a maximum period of ticks.
 */
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

    override fun tick(): InvocationResult {
        result?.let { return it }
        if (current < times) {

            val childResult = child.tick()
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

/**
 * Creates a node that will execute its child node a maximum period of ticks.
 *
 * Example:
 * ```kotlin
 * timed(20, 40) {
 *   wait(50)
 * }
 * ```
 * The node wait won't be fully finished because timed(20, 40) will return after a random amount
 * of game ticks between 20 (inclusive) and 40 (exclusive).
 *
 * @param minTicks the minimum amount of game ticks (inclusive).
 * @param maxTicks the maximum amount of game ticks (exclusive).
 */
fun TreeNodeParentBuilder<*>.timed(minTicks: Int, maxTicks: Int, builder: TreeNodeTimed.Builder.() -> Unit) =
    TreeNodeTimed.Builder(minTicks, maxTicks).also {
        addChild(it)
        builder(it)
    }
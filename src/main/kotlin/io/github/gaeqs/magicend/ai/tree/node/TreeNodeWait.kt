package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

/**
 * A waiter node. Stops the behaviour tree execution a specified amount of game ticks.
 */
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

/**
 * Creates a waiter node. This node stops the behaviour tree execution a specified amount of game ticks.
 *
 * Example:
 * ```kotlin
 * and {
 *   doSomething()
 *   wait(20)
 *   doOtherThing()
 * }
 * ```
 * doOtherThing() will be executed 20 ticks after doSomething().
 */
fun TreeNodeParentBuilder<*>.wait(ticks: Int) = addChild(TreeNodeWait.Builder(ticks))
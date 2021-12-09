package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

/**
 * A tree node that evaluates and returns the result of the given expression.
 */
class TreeNodePredicate(activity: Activity, val predicate: () -> Boolean) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        return if (predicate()) InvocationResult.SUCCESS else InvocationResult.FAIL
    }

    override fun start() {
    }

    override fun stop() {
    }

    class Builder(var predicate: () -> Boolean) : TreeNodeBuilder<TreeNodePredicate> {
        override fun build(activity: Activity) = TreeNodePredicate(activity, predicate)
    }
}

/**
 * Creates a tree node that evaluates and returns the result of the given expression.
 *
 * Example:
 * ```kotlin
 * and {
 *   predicate { kills >= 5 }
 *   evolve()
 * }
 * ```
 *
 * The node evolve() will only be executed if the entity has 5 or more kills.
 */
fun TreeNodeParentBuilder<*>.predicate(predicate: () -> Boolean) =
    addChild(TreeNodePredicate.Builder(predicate))
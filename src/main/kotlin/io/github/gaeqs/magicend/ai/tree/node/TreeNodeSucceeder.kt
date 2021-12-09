package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

/**
 * A succeeder node. This node executes its child node and returns [TreeNode.InvocationResult.SUCCESS].
 */
class TreeNodeSucceeder(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        if (child.tick() == InvocationResult.WAIT) return InvocationResult.WAIT
        return InvocationResult.SUCCESS
    }

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeSucceeder>() {
        override fun build(activity: Activity) = TreeNodeSucceeder(activity, child.build(activity))
    }
}

/**
 * Creates a succeeder node. This node executes its child node and returns [TreeNode.InvocationResult.SUCCESS].
 *
 * Example:
 * ```kotlin
 * or {
 *   succeeder {
 *      wait(20)
 *   }
 *   debug("This node is unreachable!")
 * }
 * ```
 */
inline fun TreeNodeParentBuilder<*>.succeeder(builder: TreeNodeSucceeder.Builder.() -> Unit) =
    TreeNodeSucceeder.Builder().also {
        addChild(it)
        builder(it)
    }
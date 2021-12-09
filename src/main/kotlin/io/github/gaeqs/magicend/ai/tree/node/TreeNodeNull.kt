package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

/**
 * A node that always returns [TreeNode.InvocationResult.FAIL]. Used as a placeholder for
 * [TreeNodeUniqueParentBuilders][io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder].
 */
class TreeNodeNull(activity: Activity) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        return InvocationResult.FAIL
    }

    override fun start() {
    }

    override fun stop() {
    }

    class Builder : TreeNodeBuilder<TreeNodeNull> {
        override fun build(activity: Activity) = TreeNodeNull(activity)

    }
}

/**
 * Creates a null node. This node always returns [TreeNode.InvocationResult.FAIL].
 *
 * Example:
 * ```kotlin
 * and {
 *   nul()
 *   debug("This node is unreachable!")
 * }
 * ```
 */
fun TreeNodeParentBuilder<*>.nul() = addChild(TreeNodeNull.Builder())
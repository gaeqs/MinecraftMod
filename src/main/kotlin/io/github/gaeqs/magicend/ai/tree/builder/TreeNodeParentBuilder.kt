package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode

/**
 * A [TreeNode] that may have children.
 */
interface TreeNodeParentBuilder<T : TreeNode> : TreeNodeBuilder<T> {

    /**
     * The children of this builder.
     */
    val children: List<TreeNodeBuilder<*>>

    /**
     * Adds a child to this builder.
     */
    fun addChild(node: TreeNodeBuilder<*>)

}
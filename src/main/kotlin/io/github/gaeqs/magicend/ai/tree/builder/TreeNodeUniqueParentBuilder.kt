package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import io.github.gaeqs.magicend.ai.tree.node.TreeNodeNull

/**
 * A [TreeNode] that has one and only one child.
 */
abstract class TreeNodeUniqueParentBuilder<T : TreeNode> : TreeNodeParentBuilder<T> {

    override val children: List<TreeNodeBuilder<*>> get() = listOf(child)

    /**
     * The child of this node.
     */
    var child: TreeNodeBuilder<*> = TreeNodeNull.Builder()

    /**
     * Adds a child to this node.
     * This method overrides the already set child node.
     */
    override fun addChild(node: TreeNodeBuilder<*>) {
        child = node
    }
}
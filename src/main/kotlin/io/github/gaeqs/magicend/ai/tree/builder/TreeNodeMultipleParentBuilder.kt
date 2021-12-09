package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode

/**
 * A [TreeNode] that may have multiple children.
 */
abstract class TreeNodeMultipleParentBuilder<T : TreeNode> : TreeNodeParentBuilder<T> {

    /**
     * The children of this builder. This list is mutable.
     */
    override val children = mutableListOf<TreeNodeBuilder<*>>()

    override fun addChild(node: TreeNodeBuilder<*>) {
        children += node
    }

}
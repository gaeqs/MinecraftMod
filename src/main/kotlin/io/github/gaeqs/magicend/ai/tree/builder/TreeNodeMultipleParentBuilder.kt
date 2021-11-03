package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode

abstract class TreeNodeMultipleParentBuilder<T : TreeNode> : TreeNodeParentBuilder<T> {

    override val children = mutableListOf<TreeNodeBuilder<*>>()

    override fun addChild(node: TreeNodeBuilder<*>) {
        children += node
    }

}
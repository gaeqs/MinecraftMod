package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode

abstract class TreeNodeMultipleParentBuilder<T : TreeNode> : TreeNodeParentBuilder<T> {

    private val _children = mutableListOf<TreeNodeBuilder<*>>()
    override val children: List<TreeNodeBuilder<*>> = _children

    override fun addChild(node: TreeNodeBuilder<*>) {
        _children += node
    }

}
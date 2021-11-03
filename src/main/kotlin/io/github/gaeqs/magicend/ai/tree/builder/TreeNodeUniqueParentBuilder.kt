package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import io.github.gaeqs.magicend.ai.tree.node.TreeNodeNull

abstract class TreeNodeUniqueParentBuilder<T : TreeNode> : TreeNodeParentBuilder<T> {

    override val children: List<TreeNodeBuilder<*>> get() = listOf(child)
    var child: TreeNodeBuilder<*> = TreeNodeNull.Builder()

    override fun addChild(node: TreeNodeBuilder<*>) {
        child = node
    }
}
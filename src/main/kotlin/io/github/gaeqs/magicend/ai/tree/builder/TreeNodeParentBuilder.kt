package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode

interface TreeNodeParentBuilder<T : TreeNode> : TreeNodeBuilder<T> {

    val children: List<TreeNodeBuilder<*>>

    fun addChild(node: TreeNodeBuilder<*>)

}
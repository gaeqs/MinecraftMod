package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode

abstract class TreeNodeParentBuilder<T : TreeNode> : TreeNodeBuilder<T> {

    val children = mutableListOf<TreeNodeBuilder<*>>()

}
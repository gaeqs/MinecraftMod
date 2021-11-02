package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode

interface TreeNodeBuilder<T : TreeNode> {

    fun build(): T

}
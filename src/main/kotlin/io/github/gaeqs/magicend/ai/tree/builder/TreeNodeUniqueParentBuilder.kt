package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import io.github.gaeqs.magicend.ai.tree.node.TreeNodeNull

abstract class TreeNodeUniqueParentBuilder<T : TreeNode> : TreeNodeBuilder<T> {

    var child: TreeNodeBuilder<*> = TreeNodeNull.Builder()

}
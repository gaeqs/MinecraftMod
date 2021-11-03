package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.node.TreeNode

interface TreeNodeBuilder<T : TreeNode> {

    fun build(activity: Activity): T

}
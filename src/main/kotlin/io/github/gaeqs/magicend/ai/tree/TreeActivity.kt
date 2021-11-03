package io.github.gaeqs.magicend.ai.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.EntityAI
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode

class TreeActivity(name: String, ai: EntityAI, rootProvider: TreeNodeBuilder<*>) : Activity(name, ai) {

    val root: TreeNode = rootProvider.build(this)

    override var finished = false
        private set

    override fun tick() {
        if (root() != TreeNode.InvocationResult.WAIT) {
            finished = true
        }
    }

    override fun reset() {
        finished = false
        root.reset()
    }

}
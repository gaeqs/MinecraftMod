package io.github.gaeqs.magicend.ai.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.node.TreeNode

class TreeActivity(val root: TreeNode) : Activity {

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
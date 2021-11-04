package io.github.gaeqs.magicend.ai.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.EntityAI
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode

class TreeActivity(name: String, ai: EntityAI, rootProvider: TreeNodeBuilder<*>) : Activity(name, ai) {

    val root: TreeNode = rootProvider.build(this)

    override var started = false
        private set
    override var finished = false
        private set

    override fun tick() {
        if (finished) return
        if (!started) {
            root.start()
            started = true
        }

        if (root.tick() != TreeNode.InvocationResult.WAIT) {
            root.stop()
            finished = true
        }
    }

    override fun reset() {
        if (started && !finished) {
            root.stop()
        }

        started = false
        finished = false
    }

}
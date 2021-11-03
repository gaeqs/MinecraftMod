package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeNull(activity: Activity) : TreeNode(activity) {

    override fun invoke(): InvocationResult {
        return InvocationResult.FAIL
    }

    override fun start() {
    }

    override fun stop() {
    }

    class Builder : TreeNodeBuilder<TreeNodeNull> {
        override fun build(activity: Activity) = TreeNodeNull(activity)

    }
}

fun TreeNodeParentBuilder<*>.nul() {
    children.add(TreeNodeNull.Builder())
}

fun TreeNodeUniqueParentBuilder<*>.nul() {
    child = TreeNodeNull.Builder()
}
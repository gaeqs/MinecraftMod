package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeNull private constructor() : TreeNode() {

    companion object {
        val INSTANCE = TreeNodeNull()
        val BUILDER = Builder()
    }

    override fun reset() {
    }

    override fun invoke(): InvocationResult {
        return InvocationResult.FAIL
    }

    class Builder : TreeNodeBuilder<TreeNodeNull> {
        override fun build(): TreeNodeNull = INSTANCE

    }
}

fun TreeNodeParentBuilder<*>.nul() {
    children.add(TreeNodeNull.BUILDER)
}

fun TreeNodeUniqueParentBuilder<*>.nul() {
    child = TreeNodeNull.BUILDER
}
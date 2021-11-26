package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

class TreeNodePredicate(activity: Activity, val predicate: () -> Boolean) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        return if (predicate()) InvocationResult.SUCCESS else InvocationResult.FAIL
    }

    override fun start() {
    }

    override fun stop() {
    }

    class Builder(var predicate: () -> Boolean) : TreeNodeBuilder<TreeNodePredicate> {
        override fun build(activity: Activity) = TreeNodePredicate(activity, predicate)
    }
}

fun TreeNodeParentBuilder<*>.predicate(predicate: () -> Boolean) =
    addChild(TreeNodePredicate.Builder(predicate))
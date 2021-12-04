package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.EntityAI
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeDebug(activity: Activity, val builder: (EntityAI) -> String) : TreeNode(activity) {

    override fun start() {
    }

    override fun stop() {
    }

    override fun tick(): InvocationResult {
        println(builder(ai))
        return InvocationResult.SUCCESS
    }

    class Builder(var builder: (EntityAI) -> String) : TreeNodeUniqueParentBuilder<TreeNodeDebug>() {
        override fun build(activity: Activity) = TreeNodeDebug(activity, builder)
    }
}

fun TreeNodeParentBuilder<*>.debug(builder: (EntityAI) -> String) =
    addChild(TreeNodeDebug.Builder(builder))

fun TreeNodeParentBuilder<*>.debug(string: String) =
    addChild(TreeNodeDebug.Builder { string })
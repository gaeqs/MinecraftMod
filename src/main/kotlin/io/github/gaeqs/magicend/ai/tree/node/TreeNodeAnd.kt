package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeMultipleParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

class TreeNodeAnd(activity: Activity, val children: List<TreeNode>) : TreeNode(activity) {

    private var result: InvocationResult? = null
    private var childIndex = 0

    override fun invoke(): InvocationResult {
        result?.let { return it }

        var childResult: InvocationResult = InvocationResult.SUCCESS
        while (childResult != InvocationResult.FAIL && childIndex < children.size) {
            childResult = children[childIndex]()
            if (childResult == InvocationResult.WAIT) return InvocationResult.WAIT

            children[childIndex++].stop()
            if (childResult == InvocationResult.SUCCESS && childIndex < children.size) {
                children[childIndex].start()
            }
        }
        result = childResult
        return childResult
    }

    override fun start() {
        result = null
        childIndex = 0
        if (children.isNotEmpty()) children[0].start()
    }

    override fun stop() {
        if (result == null) {
            children[childIndex].stop()
        }
    }

    class Builder : TreeNodeMultipleParentBuilder<TreeNodeAnd>() {
        override fun build(activity: Activity) = TreeNodeAnd(activity, children.map { it.build(activity) })
    }
}

inline fun TreeNodeParentBuilder<*>.and(builder: TreeNodeAnd.Builder.() -> Unit) = TreeNodeAnd.Builder().also {
    addChild(it)
    builder(it)
}

inline fun and(builder: TreeNodeAnd.Builder.() -> Unit): TreeNodeAnd.Builder =
    TreeNodeAnd.Builder().also { builder(it) }
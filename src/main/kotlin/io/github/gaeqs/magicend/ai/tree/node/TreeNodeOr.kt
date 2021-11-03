package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeMultipleParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

class TreeNodeOr(activity: Activity, val children: List<TreeNode>) : TreeNode(activity) {

    private var result: InvocationResult? = null
    private var childIndex = 0

    override fun tick(): InvocationResult {
        result?.let { return it }

        var childResult: InvocationResult = InvocationResult.FAIL
        while (childResult != InvocationResult.SUCCESS && childIndex < children.size) {
            childResult = children[childIndex].tick()
            if (childResult == InvocationResult.WAIT) return InvocationResult.WAIT

            children[childIndex++].stop()
            if (childResult == InvocationResult.FAIL && childIndex < children.size) {
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

    class Builder : TreeNodeMultipleParentBuilder<TreeNodeOr>() {
        override fun build(activity: Activity) = TreeNodeOr(activity, children.map { it.build(activity) })
    }
}

inline fun TreeNodeParentBuilder<*>.or(builder: TreeNodeOr.Builder.() -> Unit) = TreeNodeOr.Builder().also {
    addChild(it)
    builder(it)
}

inline fun or(builder: TreeNodeOr.Builder.() -> Unit): TreeNodeOr.Builder {
    return TreeNodeOr.Builder().also(builder)
}
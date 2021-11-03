package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeOr(activity: Activity, val children: List<TreeNode>) : TreeNode(activity) {

    private var result: InvocationResult? = null
    private var childIndex = 0

    override fun reset() {
        result = null
        childIndex = 0
        children.forEach { it.reset() }
    }

    override fun invoke(): InvocationResult {
        result?.let { return it }

        var childResult: InvocationResult = InvocationResult.FAIL
        while (childResult != InvocationResult.SUCCESS && childIndex < children.size) {
            childResult = children[childIndex]()
            if (childResult == InvocationResult.WAIT) return InvocationResult.WAIT
            childIndex++
        }
        result = childResult
        return childResult
    }

    class Builder : TreeNodeParentBuilder<TreeNodeOr>() {
        override fun build(activity: Activity) = TreeNodeOr(activity, children.map { it.build(activity) })
    }
}

inline fun TreeNodeParentBuilder<*>.or(builder: TreeNodeOr.Builder.() -> Unit) {
    val b = TreeNodeOr.Builder()
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.or(builder: TreeNodeOr.Builder.() -> Unit) {
    val b = TreeNodeOr.Builder()
    child = b
    builder(b)
}

inline fun or(builder: TreeNodeOr.Builder.() -> Unit): TreeNodeOr.Builder {
    return TreeNodeOr.Builder().also { builder(it) }
}
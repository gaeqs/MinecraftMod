package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeAnd(val children: List<TreeNode>) : TreeNode() {

    private var result: InvocationResult? = null
    private var childIndex = 0

    override fun reset() {
        result = null
        childIndex = 0
        children.forEach { it.reset() }
    }

    override fun invoke(): InvocationResult {
        result?.let { return it }

        var childResult: InvocationResult = InvocationResult.SUCCESS
        while (childResult != InvocationResult.FAIL && childIndex < children.size) {
            childResult = children[childIndex]()
            if (childResult == InvocationResult.WAIT) return InvocationResult.WAIT
            childIndex++
        }
        result = childResult
        return childResult
    }

    class Builder : TreeNodeParentBuilder<TreeNodeAnd>() {
        override fun build() = TreeNodeAnd(children.map { it.build() })
    }
}

inline fun TreeNodeParentBuilder<*>.and(builder: TreeNodeAnd.Builder.() -> Unit) {
    val b = TreeNodeAnd.Builder()
    children.add(b)
    builder(b)
}

inline fun TreeNodeUniqueParentBuilder<*>.and(builder: TreeNodeAnd.Builder.() -> Unit) {
    val b = TreeNodeAnd.Builder()
    child = b
    builder(b)
}
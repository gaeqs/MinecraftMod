package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeMultipleParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

/**
 * Represents the conditional tree node (?).
 */
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

/**
 * Creates a conditional (?) tree node. You can use the builder param to create children.
 *
 * Example:
 * ```kotlin
 * or {
 *   myCustomNode()
 *   wait(20)
 * }
 * ```
 * If myCustomNode succeeds, the wait node won't be executed and this conditional
 * node will return [TreeNode.InvocationResult.SUCCESS]
 */
inline fun TreeNodeParentBuilder<*>.or(builder: TreeNodeOr.Builder.() -> Unit) = TreeNodeOr.Builder().also {
    addChild(it)
    builder(it)
}

/**
 * Creates a conditional (?) tree node as a root node. You can use the builder param to create children.
 *
 * Example:
 * ```kotlin
 * or {
 *   myCustomNode()
 *   wait(20)
 * }
 * ```
 * If myCustomNode succeeds, the wait node won't be executed and this conditional
 * node will return [TreeNode.InvocationResult.SUCCESS]
 */
inline fun rootOr(builder: TreeNodeOr.Builder.() -> Unit): TreeNodeOr.Builder {
    return TreeNodeOr.Builder().also(builder)
}
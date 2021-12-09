package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeMultipleParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

/**
 * Represents the sequence tree node (->).
 */
class TreeNodeAnd(activity: Activity, val children: List<TreeNode>) : TreeNode(activity) {

    private var result: InvocationResult? = null
    private var childIndex = 0

    override fun tick(): InvocationResult {
        result?.let { return it }

        var childResult: InvocationResult = InvocationResult.SUCCESS
        while (childResult != InvocationResult.FAIL && childIndex < children.size) {
            childResult = children[childIndex].tick()
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

/**
 * Creates a sequence (->) tree node. You can use the builder param to create children.
 *
 * Example:
 * ```kotlin
 * and {
 *   myCustomNode()
 *   wait(20)
 * }
 * ```
 * If myCustomNode fails, the wait node won't be executed and this sequence
 * node will return [TreeNode.InvocationResult.FAIL]
 */
inline fun TreeNodeParentBuilder<*>.and(builder: TreeNodeAnd.Builder.() -> Unit) = TreeNodeAnd.Builder().also {
    addChild(it)
    builder(it)
}

/**
 * Creates a sequence (->) tree node as a root. You can use the builder param to create children.
 *
 * Example:
 * ```kotlin
 * and {
 *   myCustomNode()
 *   wait(20)
 * }
 * ```
 * If myCustomNode fails, the wait node won't be executed and this sequence
 * node will return [TreeNode.InvocationResult.FAIL]
 */
inline fun rootAnd(builder: TreeNodeAnd.Builder.() -> Unit): TreeNodeAnd.Builder =
    TreeNodeAnd.Builder().also(builder)
package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeMultipleParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNodeSimultaneously.Mode

/**
 * A node that runs its children nodes simultaneously.
 * It can behave as an "and" or "or" node, but it always waits for all nodes to finish.
 * If this node's mode is [Mode.FIRST], this node returns when a node
 * finishes its job, returning the finished job's result.
 */
class TreeNodeSimultaneously(activity: Activity, val mode: Mode, val children: List<TreeNode>) : TreeNode(activity) {

    private var result: InvocationResult? = null
    private val executingChildren = mutableListOf<TreeNode>()


    override fun tick(): InvocationResult {
        if (executingChildren.isEmpty()) return result ?: InvocationResult.SUCCESS

        val iterator = executingChildren.iterator()
        while (iterator.hasNext()) {
            val child = iterator.next()
            val result = child.tick()

            if (result == InvocationResult.WAIT) continue
            iterator.remove()

            when (mode) {
                Mode.OR -> {
                    if (result == InvocationResult.SUCCESS) {
                        this.result = InvocationResult.SUCCESS
                    } else if (this.result == null) {
                        this.result = InvocationResult.FAIL
                    }
                }
                Mode.AND -> {
                    if (result == InvocationResult.FAIL) {
                        this.result = InvocationResult.FAIL
                    } else if (this.result == null) {
                        this.result = InvocationResult.SUCCESS
                    }
                }
                Mode.FIRST -> {
                    this.result = result
                }
            }

            if (result == InvocationResult.SUCCESS && mode == Mode.OR) {
                this.result = InvocationResult.SUCCESS
            }

        }

        return if (executingChildren.isEmpty() || mode == Mode.FIRST && result != null) result!!
        else InvocationResult.WAIT
    }

    override fun start() {
        result = null
        executingChildren += children
        children.forEach { it.start() }
    }

    override fun stop() {
        executingChildren.forEach { it.stop() }
    }

    class Builder(var mode: Mode) : TreeNodeMultipleParentBuilder<TreeNodeSimultaneously>() {
        override fun build(activity: Activity) =
            TreeNodeSimultaneously(activity, mode, children.map { it.build(activity) })
    }


    enum class Mode {
        OR, AND, FIRST
    }
}

/**
 * Creates a node that runs its children nodes simultaneously.
 * It can behave as an "and" or "or" node.
 * If this node's mode is [Mode.FIRST], this node returns when a node finishes its job.
 *
 * Example:
 * ```kotlin
 * // This tree waits 50 game ticks and returns SUCCESS
 * simultaneously(TreeNodeSimultaneously.Mode.OR) {
 *   failer {
 *     wait(50)
 *   }
 *   wait(20)
 * }
 *
 * // This tree waits 50 game ticks and returns FAIL
 * simultaneously(TreeNodeSimultaneously.Mode.AND) {
 *   failer {
 *     wait(50)
 *   }
 *   wait(20)
 * }
 *
 * // This tree waits 20 game ticks and returns SUCCESS
 * simultaneously(TreeNodeSimultaneously.Mode.FIRST) {
 *   failer {
 *     wait(50)
 *   }
 *   wait(20)
 * }
 * ```
 */
inline fun TreeNodeParentBuilder<*>.simultaneously(
    mode: Mode = Mode.OR,
    builder: TreeNodeSimultaneously.Builder.() -> Unit
) = TreeNodeSimultaneously.Builder(mode).also {
    addChild(it)
    builder(it)
}

/**
 * Creates a root node that runs its children nodes simultaneously.
 * It can behave as an "and" or "or" node.
 * If this node's mode is [Mode.FIRST], this node returns when a node finishes its job.
 *
 * Example:
 * ```kotlin
 * // This tree waits 50 game ticks and returns SUCCESS
 * simultaneously(TreeNodeSimultaneously.Mode.OR) {
 *   failer {
 *     wait(50)
 *   }
 *   wait(20)
 * }
 *
 * // This tree waits 50 game ticks and returns FAIL
 * simultaneously(TreeNodeSimultaneously.Mode.AND) {
 *   failer {
 *     wait(50)
 *   }
 *   wait(20)
 * }
 *
 * // This tree waits 20 game ticks and returns SUCCESS
 * simultaneously(TreeNodeSimultaneously.Mode.FIRST) {
 *   failer {
 *     wait(50)
 *   }
 *   wait(20)
 * }
 * ```
 */
inline fun rootSimultaneously(
    mode: TreeNodeSimultaneously.Mode = TreeNodeSimultaneously.Mode.OR,
    builder: TreeNodeSimultaneously.Builder.() -> Unit
) = TreeNodeSimultaneously.Builder(mode).also { builder(it) }
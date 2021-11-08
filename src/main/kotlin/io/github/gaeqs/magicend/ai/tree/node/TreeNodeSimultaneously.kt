package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeMultipleParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

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
            }

            if (result == InvocationResult.SUCCESS && mode == Mode.OR) {
                this.result = InvocationResult.SUCCESS
            }

        }

        return if (executingChildren.isEmpty()) result!! else InvocationResult.WAIT
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
        OR, AND
    }
}

inline fun TreeNodeParentBuilder<*>.simultaneously(
    mode: TreeNodeSimultaneously.Mode = TreeNodeSimultaneously.Mode.OR,
    builder: TreeNodeSimultaneously.Builder.() -> Unit
) = TreeNodeSimultaneously.Builder(mode).also {
    addChild(it)
    builder(it)
}
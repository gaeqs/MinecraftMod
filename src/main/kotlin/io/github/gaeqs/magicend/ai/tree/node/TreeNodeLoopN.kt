package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

/**
 * A loop node that executes its child node N times.
 */
class TreeNodeLoopN(activity: Activity, val child: TreeNode, val times: Int) : TreeNode(activity) {

    private var executed = 0

    override fun tick(): InvocationResult {
        while (executed < times) {
            if (child.tick() == InvocationResult.WAIT) return InvocationResult.WAIT
            executed++
            if (executed < times) {
                child.stop()
                child.start()
            }
        }
        return InvocationResult.SUCCESS
    }

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder(var times: Int) : TreeNodeUniqueParentBuilder<TreeNodeLoopN>() {
        override fun build(activity: Activity) = TreeNodeLoopN(activity, child.build(activity), times)
    }
}

/**
 * Creates a loop node that executes its child node N times.
 *
 * Example:
 * ```kotlin
 * loopN(5) {
 *   debug("Hi!")
 * }
 * ```
 * This example prints "Hi!" five times.
 *
 * @param times the number of times the child will be executed.
 *
 */
inline fun TreeNodeParentBuilder<*>.loopN(times: Int, builder: TreeNodeLoopN.Builder.() -> Unit) =
    TreeNodeLoopN.Builder(times).also {
        addChild(it)
        builder(it)
    }


/**
 * Creates a loop node that executes its child node N times as a root node.
 *
 * Example:
 * ```kotlin
 * loopN(5) {
 *   lambda {
 *      tick {
 *         println("Hi!")
 *         TreeNode.InvocationResult.SUCCESS
 *      }
 *   }
 * }
 * ```
 * This example prints "Hi!" five times.
 *
 * @param times the number of times the child will be executed.
 *
 */
inline fun loopN(times: Int, builder: TreeNodeLoopN.Builder.() -> Unit): TreeNodeLoopN.Builder {
    return TreeNodeLoopN.Builder(times).also(builder)
}
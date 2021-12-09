package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

/**
 * A loop tree node that loops its child node unconditionally. This node never returns.
 * Use this node as a root or with caution!
 */
class TreeNodeLoopUnconditional(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        while (child.tick() != InvocationResult.WAIT) {
            child.stop()
            child.start()
        }
        return InvocationResult.WAIT
    }

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeLoopUnconditional>() {
        override fun build(activity: Activity) = TreeNodeLoopUnconditional(activity, child.build(activity))
    }
}

/**
 * Creates a loop tree node that loops its child node unconditionally without returning.
 * Use this node with caution!
 *
 * Example:
 * ```kotlin
 * and {
 *   loopUnconditionally {
 *     debug("Hi!")
 *   }
 *   debug("This node is unreachable!")
 * }
 * ```
 */
inline fun TreeNodeParentBuilder<*>.loopUnconditional(builder: TreeNodeLoopUnconditional.Builder.() -> Unit) =
    TreeNodeLoopUnconditional.Builder().also {
        addChild(it)
        builder(it)
    }

/**
 * Creates a root loop tree node that executes its child node unconditionally without returning.
 * Example:
 * ```kotlin
 * and {
 *   loopUnconditionally {
 *     debug("Hi!")
 *   }
 *   debug("This node is unreachable")
 * }
 * ```
 */
inline fun rootLoopUnconditional(builder: TreeNodeLoopUnconditional.Builder.() -> Unit): TreeNodeLoopUnconditional.Builder {
    return TreeNodeLoopUnconditional.Builder().also(builder)
}
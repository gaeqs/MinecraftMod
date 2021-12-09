package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

/**
 * A loop tree node that loops its child node until it fails.
 */
class TreeNodeLoopUntilFail(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun tick(): InvocationResult {
        while (true) {
            when (child.tick()) {
                InvocationResult.WAIT -> return InvocationResult.WAIT
                InvocationResult.FAIL -> return InvocationResult.SUCCESS
                else -> {
                    child.stop()
                    child.start()
                }
            }
        }
    }

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeLoopUntilFail>() {
        override fun build(activity: Activity) = TreeNodeLoopUntilFail(activity, child.build(activity))
    }
}

/**
 * Creates a loop tree node that loops its child until it fails.
 *
 * Example:
 * ```kotlin
 * loopUntilFail {
 *   inverter {
 *     isEntityNearby()
 *   }
 *   succeeder {
 *     walkToEntity()
 *   }
 * }
 * ```
 */
inline fun TreeNodeParentBuilder<*>.loopUntilFail(builder: TreeNodeLoopUntilFail.Builder.() -> Unit) =
    TreeNodeLoopUntilFail.Builder().also {
        addChild(it)
        builder(it)
    }

/**
 * Creates a root loop tree node that loops its child until it fails.
 *
 * Example:
 * ```kotlin
 * loopUntilFail {
 *   inverter {
 *     isEntityNearby()
 *   }
 *   succeeder {
 *     walkToEntity()
 *   }
 * }
 * ```
 */
inline fun rootLoopUntilFail(builder: TreeNodeLoopUntilFail.Builder.() -> Unit): TreeNodeLoopUntilFail.Builder {
    return TreeNodeLoopUntilFail.Builder().also(builder)
}
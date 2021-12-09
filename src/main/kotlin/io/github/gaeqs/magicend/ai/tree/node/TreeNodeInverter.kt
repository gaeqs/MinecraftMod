package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

/**
 * A node that inverts the result of its child node.
 */
class TreeNodeInverter(activity: Activity, val child: TreeNode) : TreeNode(activity) {

    override fun tick() = child.tick().not()

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder : TreeNodeUniqueParentBuilder<TreeNodeInverter>() {
        override fun build(activity: Activity) = TreeNodeInverter(activity, child.build(activity))
    }
}

/**
 * Creates an inverter node. This node inverts the result of its child node.
 *
 *  * Exmaple:
 * ```kotlin
 * and {
 *   inverter {
 *      wait(20)
 *   }
 *   wait(50)
 * }
 * ```
 * In this behaviour tree wait(50) will never be executed,
 * as wait(20) always returns [TreeNode.InvocationResult.SUCCESS].
 */
inline fun TreeNodeParentBuilder<*>.inverter(builder: TreeNodeInverter.Builder.() -> Unit) =
    TreeNodeInverter.Builder().also {
        addChild(it)
        builder(it)
    }
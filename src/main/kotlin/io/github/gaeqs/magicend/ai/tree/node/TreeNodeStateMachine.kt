package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.statemachine.StateMachine
import io.github.gaeqs.magicend.ai.statemachine.builder.StateMachineBuilder
import io.github.gaeqs.magicend.ai.statemachine.node.StateMachineNode
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder

/**
 * A tree node that executes a state machine inside.
 *
 * This node includes the states "fail" and "success" that finishes the state machine with the corresponding result.
 */
class TreeNodeStateMachine(
    activity: Activity,
    builder: StateMachineBuilder
) : TreeNode(activity), StateMachine {

    init {
        if (builder.nodes.any { it.name == "fail" }) {
            throw IllegalArgumentException("'fail' is a reserved state machine in a tree node state machine!")
        }
        if (builder.nodes.any { it.name == "success" }) {
            throw IllegalArgumentException("'success' is a reserved state machine in a tree node machine activity!")
        }
    }

    private val failNode: StateMachineNode =
        object : StateMachineNode("fail", activity, this) {
            override fun start() {}
            override fun tick() {}
            override fun stop() {}
        }

    private val successNode: StateMachineNode =
        object : StateMachineNode("success", activity, this) {
            override fun start() {}
            override fun tick() {}
            override fun stop() {}
        }

    private val _nodes = (builder.nodes.map { it.build(activity, this) } + listOf(failNode, successNode))
        .associateBy { it.name }
    override val nodes: Set<StateMachineNode> = _nodes.values.toSet()
    override val startNode = _nodes[builder.startNode]
        ?: throw IllegalArgumentException("Couldn't find start node ${builder.startNode}!")
    override var currentNode: StateMachineNode = startNode

    private var started = false

    override fun start() {
        currentNode = startNode
        currentNode.start()
        started = true
    }

    override fun stop() {
        currentNode.stop()
        started = false
    }

    override fun tick(): InvocationResult {
        currentNode.tick()
        return when (currentNode) {
            failNode -> InvocationResult.FAIL
            successNode -> InvocationResult.SUCCESS
            else -> InvocationResult.WAIT
        }
    }

    override fun changeState(node: String) {
        if (started) currentNode.stop()
        currentNode = _nodes[node] ?: throw IllegalArgumentException("Couldn't find node $node!")
        if (started) currentNode.start()
    }

    class Builder(
        var builder: StateMachineBuilder
    ) : TreeNodeBuilder<TreeNodeStateMachine> {
        override fun build(activity: Activity) = TreeNodeStateMachine(activity, builder)
    }
}

/**
 * A tree node that executes a state machine inside.
 *
 * This node includes the states "fail" and "success" that finishes the state machine with the corresponding result.
 *
 * Example:
 * ```kotlin
 * stateMachine {
 *   startNode = "start"
 *   lambda("start") {
 *     tick {
 *       if (random.nextBoolean()) {
 *         changeState(if (random.nextBoolean()) "fail" else "success")
 *       }
 *     }
 *   }
 * ```
 */
inline fun TreeNodeParentBuilder<*>.stateMachine(builder: StateMachineBuilder.() -> Unit) =
    TreeNodeStateMachine.Builder(StateMachineBuilder().apply(builder)).also(::addChild)

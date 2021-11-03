package io.github.gaeqs.magicend.ai.statemachine

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.EntityAI
import io.github.gaeqs.magicend.ai.statemachine.builder.StateMachineBuilder
import io.github.gaeqs.magicend.ai.statemachine.node.StateMachineNode

class StateMachineActivity(name: String, ai: EntityAI, builder: StateMachineBuilder) : Activity(name, ai),
    StateMachine {

    override var started: Boolean = false
        private set
    override var finished: Boolean = false
        private set

    private val _nodes = builder.nodes.map { it.build(this, this) }.associateBy { it.name }
    override val nodes: Set<StateMachineNode> = _nodes.values.toSet()

    override val startNode = _nodes[builder.startNode]
        ?: throw IllegalArgumentException("Couldn't find start node ${builder.startNode}!")

    override var currentNode: StateMachineNode = startNode

    override val endNode: StateMachineNode =
        object : StateMachineNode("end", this, this) {
            override fun start() {
                finished = true
            }

            override fun tick() {
            }

            override fun stop() {
            }
        }

    override fun tick() {
        if (!started) {
            currentNode.start()
        }
        if (!finished) {
            currentNode.tick()
        }
    }

    override fun reset() {
        if (started && !finished) {
            currentNode.stop()
        }

        started = false
        finished = false
        currentNode = startNode
    }

    override fun changeState(node: String) {
        if (started) currentNode.stop()
        currentNode = _nodes[node] ?: throw IllegalArgumentException("Couldn't find node $node!")
        if (started) currentNode.start()
    }


}
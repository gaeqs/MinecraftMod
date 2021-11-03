package io.github.gaeqs.magicend.ai.statemachine

import io.github.gaeqs.magicend.ai.statemachine.node.StateMachineNode

interface StateMachine {

    val nodes: Set<StateMachineNode>
    val currentNode: StateMachineNode

    val startNode: StateMachineNode
    val endNode: StateMachineNode

    fun changeState(node: String)

}
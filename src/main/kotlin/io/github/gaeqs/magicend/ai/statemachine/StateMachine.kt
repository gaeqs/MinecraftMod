package io.github.gaeqs.magicend.ai.statemachine

import io.github.gaeqs.magicend.ai.statemachine.node.StateMachineNode

/**
 * Basic interface for a state machine.
 */
interface StateMachine {

    /**
     * The states of this state machine.
     */
    val nodes: Set<StateMachineNode>

    /**
     * The current state of this state machine.
     */
    val currentNode: StateMachineNode

    /**
     * The start node of this state machine.
     */
    val startNode: StateMachineNode

    /**
     * Changes the state of this state machine.
     * @param node the new state.
     * @throws IllegalArgumentException if there is no node that matches the given name.
     */
    fun changeState(node: String)

}
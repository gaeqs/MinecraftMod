package io.github.gaeqs.magicend.ai.statemachine.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.Node
import io.github.gaeqs.magicend.ai.statemachine.StateMachine

/**
 * Represents a [Node] inside a state machine. This node represents a state.
 *
 * @param name the name of the state.
 * @param activity the activity of this node.
 * @param machine the machine this node is inside of.
 */
abstract class StateMachineNode(val name: String, activity: Activity, val machine: StateMachine) : Node(activity) {

    /**
     * This method is invoked when the node starts its execution.
     * This can be used to initialize data before [tick]
     */
    abstract fun start()

    /**
     * Ticks this state. Implement the basic logic of the state here.
     */
    abstract fun tick()

    /**
     * This method is called just after the node finishes its execution.
     * Use this method to clean data.
     */
    abstract fun stop()

    /**
     * Changes the state of this state machine.
     * @param node the new state.
     * @throws IllegalArgumentException if there is no node that matches the given name.
     */
    fun changeState(node: String) {
        machine.changeState(node)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as StateMachineNode
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}
package io.github.gaeqs.magicend.ai.statemachine.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.Node
import io.github.gaeqs.magicend.ai.statemachine.StateMachine

abstract class StateMachineNode(val name: String, activity: Activity, val machine: StateMachine) : Node(activity) {

    abstract fun start()

    abstract fun tick()

    abstract fun stop()

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
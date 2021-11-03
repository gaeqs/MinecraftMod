package io.github.gaeqs.magicend.ai.statemachine.builder

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.statemachine.StateMachine
import io.github.gaeqs.magicend.ai.statemachine.node.StateMachineNode

abstract class StateMachineNodeBuilder<T : StateMachineNode>(val name : String) {

    abstract fun build(activity: Activity, stateMachine: StateMachine): T

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as StateMachineNodeBuilder<*>
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}
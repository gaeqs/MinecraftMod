package io.github.gaeqs.magicend.ai.statemachine.builder

class StateMachineBuilder {

    var nodes = mutableSetOf<StateMachineNodeBuilder<*>>()
    var startNode: String = "start"

}

fun stateMachine(builder: StateMachineBuilder.() -> Unit) = StateMachineBuilder().apply(builder)
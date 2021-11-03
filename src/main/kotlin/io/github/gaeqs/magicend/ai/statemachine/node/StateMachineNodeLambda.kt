package io.github.gaeqs.magicend.ai.statemachine.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.statemachine.StateMachine
import io.github.gaeqs.magicend.ai.statemachine.builder.StateMachineBuilder
import io.github.gaeqs.magicend.ai.statemachine.builder.StateMachineNodeBuilder

class StateMachineNodeLambda(
    name: String,
    activity: Activity,
    stateMachine: StateMachine,
    val start: StateMachineNodeLambda.() -> Unit,
    val tick: StateMachineNodeLambda.() -> Unit,
    val stop: StateMachineNodeLambda.() -> Unit,
) : StateMachineNode(name, activity, stateMachine) {

    override fun tick() = tick(this)
    override fun start() = start(this)
    override fun stop() = stop(this)

    class Builder(
        name: String,
        var start: StateMachineNodeLambda.() -> Unit = { },
        var tick: StateMachineNodeLambda.() -> Unit = { },
        var stop: StateMachineNodeLambda.() -> Unit = { },
    ) : StateMachineNodeBuilder<StateMachineNodeLambda>(name) {
        
        override fun build(activity: Activity, stateMachine: StateMachine) =
            StateMachineNodeLambda(name, activity, stateMachine, start, tick, stop)

        fun start(start: StateMachineNodeLambda.() -> Unit) {
            this.start = start
        }

        fun tick(tick: StateMachineNodeLambda.() -> Unit) {
            this.tick = tick
        }

        fun stop(stop: StateMachineNodeLambda.() -> Unit) {
            this.stop = stop
        }

    }
}

inline fun StateMachineBuilder.lambda(name: String, builder: StateMachineNodeLambda.Builder.() -> Unit) =
    StateMachineNodeLambda.Builder(name).also {
        nodes += it
        builder(it)
    }
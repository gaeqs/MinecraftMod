package io.github.gaeqs.magicend.ai.statemachine

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.EntityAI

class StateMachineActivity(name: String, ai: EntityAI) : Activity(name, ai) {

    override val started: Boolean = false
    override val finished: Boolean = false

    override fun tick() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }


}
package io.github.gaeqs.magicend.ai

interface Activity {

    val finished: Boolean

    fun tick()

    fun reset()

}
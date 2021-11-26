package io.github.gaeqs.magicend.ai.memory

class Memory<T>(val value: T, private val validTillTick: Long = -1) {

    fun isValid(currentTick: Long) = validTillTick < 0 || validTillTick > currentTick

}
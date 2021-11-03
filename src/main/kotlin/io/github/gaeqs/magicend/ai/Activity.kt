package io.github.gaeqs.magicend.ai

abstract class Activity(val name: String, val ai: EntityAI) {

    abstract val started: Boolean
    abstract val finished: Boolean

    abstract fun tick()

    abstract fun reset()


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Activity
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}
package io.github.gaeqs.magicend.ai

/**
 * Represents an activity a [EntityAI] can perform.
 *
 * @param name the name of the activity. This name must be unique and represents this activity.
 * @param ai the [EntityAI] this activity controls.
 */
abstract class Activity(val name: String, val ai: EntityAI) {

    /**
     * Whether the activity has started.
     */
    abstract val started: Boolean

    /**
     * Whether the activity has finished.
     */
    abstract val finished: Boolean

    /**
     * Ticks the activity.
     */
    abstract fun tick()

    /**
     * Resets the activity.
     */
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
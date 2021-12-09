package io.github.gaeqs.magicend.ai

import io.github.gaeqs.magicend.ai.memory.Memory
import io.github.gaeqs.magicend.ai.memory.MemoryType
import net.minecraft.entity.LivingEntity

/**
 * Represents the artificial intelligence of a [LivingEntity].
 *
 * The artificial intelligence contains a set of [Activity].
 * These activities are executed at the same time. You can add and remove activities at runtime.
 *
 * The artificial intelligence also contains a core activity. This activity
 * won't be removed when [runExclusively] is invoked.
 *
 * Activities will be removed from the artificial intelligence when they're finished.
 *
 * Instances of this class also have the capacity of remember elements.
 * Use [remember], [forget], [hasMemory] and [getMemory] to manipulate this
 * memory.
 *
 * @param entity the entity this artificial intelligence is controlling.
 */
class EntityAI(val entity: LivingEntity) {

    /**
     * The core activity. This activity won't be removed when [runExclusively] is invoked.
     */
    var coreActivity: Activity? = null

    /**
     * The set containing all running activities of this AI.
     * This set doesn't contain the core activity.
     */
    val activities = mutableSetOf<Activity>()

    /**
     * The current tick of this AI.
     */
    var tick = 0L
        private set

    private val tickingActivities = arrayListOf<Activity>()
    private val memories = mutableMapOf<MemoryType<*>, Memory<*>>()

    /**
     * Ticks the AI.
     */
    fun tick() {
        tickActivities()
        tick++
    }

    /**
     * Runs the given [Activity], removing all other activities (except the core activity).
     * @param activities the [Activity] to run.
     */
    fun runExclusively(vararg activities: Activity) {
        this.activities.clear()
        this.activities.addAll(activities)
    }

    private fun tickActivities() {
        coreActivity?.tick()
        if (coreActivity?.finished == true) {
            coreActivity = null
        }

        tickingActivities.clear()
        tickingActivities.addAll(activities)
        tickingActivities.forEach { it.tick() }
        activities.removeIf { it.finished }
    }

    // region memory

    /**
     * Adds the given memory to this AI.
     *
     * You can specify a duration in ticks for this memory.
     *
     * @param type the [MemoryType]. This is the "key" where the memory will be stored.
     * @param value the memory.
     * @param duration the duration of the memory. Set a negative value if you want to set the memory without time.
     */
    fun <T> remember(type: MemoryType<T>, value: T, duration: Long = -1) {
        memories[type] = if (duration < 0) {
            Memory(value)
        } else {
            Memory(value, tick + duration)
        }
    }

    /**
     * Forgets the memory that matches the given [MemoryType].
     * @param type the [MemoryType].
     */
    fun forget(type: MemoryType<*>) {
        memories.remove(type)
    }

    /**
     * @return whether this AI has a valid memory of the given [MemoryType].
     */
    fun hasMemory(type: MemoryType<*>): Boolean {
        val memory = memories[type] ?: return false
        if (!memory.isValid(tick)) {
            memories.remove(type)
            return false
        }
        return true
    }

    /**
     * @return the memory that matches the given [MemoryType] or null if not present.
     */
    fun <T> getMemory(type: MemoryType<T>): T? {
        val memory = memories[type] ?: return null
        if (!memory.isValid(tick)) {
            memories.remove(type)
            return null
        }

        return try {
            @Suppress("UNCHECKED_CAST")
            memory.value as T
        } catch (ex: ClassCastException) {
            null
        }
    }

    // endregion

}
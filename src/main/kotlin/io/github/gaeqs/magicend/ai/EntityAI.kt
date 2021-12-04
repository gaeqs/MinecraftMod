package io.github.gaeqs.magicend.ai

import io.github.gaeqs.magicend.ai.memory.Memory
import io.github.gaeqs.magicend.ai.memory.MemoryType
import net.minecraft.entity.LivingEntity

class EntityAI(val entity: LivingEntity) {

    var coreActivity: Activity? = null
    val activities = mutableSetOf<Activity>()
    private val tickingActivities = arrayListOf<Activity>()

    var tick = 0L
        private set

    private val memories = mutableMapOf<MemoryType<*>, Memory<*>>()

    fun tick() {
        tickActivities()
        tick++
    }

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

    fun <T> remember(type: MemoryType<T>, value: T, duration: Long = -1) {
        memories[type] = if (duration < 0) {
            Memory(value)
        } else {
            Memory(value, tick + duration)
        }
    }

    fun forget(type: MemoryType<*>) {
        memories.remove(type)
    }

    fun hasMemory(type: MemoryType<*>): Boolean {
        val memory = memories[type] ?: return false
        if (!memory.isValid(tick)) {
            memories.remove(type)
            return false
        }
        return true
    }

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
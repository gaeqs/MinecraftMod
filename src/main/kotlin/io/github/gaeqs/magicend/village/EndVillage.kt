package io.github.gaeqs.magicend.village

import io.github.gaeqs.magicend.entity.EnderVillager
import io.github.gaeqs.magicend.entity.GuardianEnderman
import java.util.concurrent.atomic.AtomicInteger

class EndVillage {

    companion object {
        private val ID_GENERATOR = AtomicInteger()
    }

    val id = ID_GENERATOR.getAndIncrement()

    private val _villagers = mutableSetOf<EnderVillager>()
    val villagers: Set<EnderVillager> get() = _villagers.apply { if (removeIf { !it.isAlive }) callToPatrol() }
    val guardians get() = villagers.filterIsInstance<GuardianEnderman>()

    fun add(villager: EnderVillager): Boolean {
        if (_villagers.add(villager)) {
            callToPatrol()
            return true
        }
        return false
    }

    fun refresh() {
        villagers
    }

    fun remove(villager: EnderVillager): Boolean {
        if (_villagers.remove(villager)) {
            callToPatrol()
            return true
        }
        return false
    }

    fun callToPatrol() {
        val guardians = guardians

        val patrolling = guardians.find { it.patrolling }
        if (patrolling != null) {
            guardians.filter { it.patrolling && it != patrolling }.forEach { it.patrolling = false }
            return
        }

        val guardian = guardians.minByOrNull { it.lastTimePatrolling } ?: return
        guardian.patrolling = true
    }

    override fun toString() = "Village $id"
}
package io.github.gaeqs.magicend.village

import io.github.gaeqs.magicend.entity.EnderVillager
import java.util.concurrent.atomic.AtomicInteger

class EndVillage {

    companion object {
        private val ID_GENERATOR = AtomicInteger()
    }

    private val _villagers = mutableSetOf<EnderVillager>()

    val id = ID_GENERATOR.getAndIncrement();
    val villagers: Set<EnderVillager> = _villagers

    fun add(villager: EnderVillager) = _villagers.add(villager)
    fun remove(villager: EnderVillager) = _villagers.remove(villager)

    override fun toString() = "Village $id"
}
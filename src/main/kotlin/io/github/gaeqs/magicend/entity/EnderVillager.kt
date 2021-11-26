package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.village.EndVillage
import net.minecraft.entity.EntityType
import net.minecraft.particle.ParticleTypes
import net.minecraft.world.World

open class EnderVillager(
    type: EntityType<out EnderVillager>,
    world: World
) : AIEntity(type, world) {

    var village: EndVillage
        private set

    init {
        village = EndVillage().apply { add(this@EnderVillager) }
    }


    override fun tickMovement() {
        if (world.isClient) {
            repeat(2) {
                world.addParticle(
                    ParticleTypes.PORTAL,
                    getParticleX(0.5),
                    this.randomBodyY - 0.25,
                    getParticleZ(0.5),
                    (random.nextDouble() - 0.5) * 2.0,
                    -random.nextDouble(),
                    (random.nextDouble() - 0.5) * 2.0
                )
            }
        }
        if (random.nextDouble() > 0.99) {
            searchBiggerVillages()
        }

        super.tickMovement()
    }

    private fun searchBiggerVillages() {
        val box = boundingBox.expand(64.0, 32.0, 64.0)
        val other = world.getEntitiesByClass(EnderVillager::class.java, box) {
            it != this && it.isAlive && it.village != village && it.village.villagers.size > village.villagers.size
        }.maxByOrNull { it.village.villagers.size } ?: return

        village.remove(this)
        village = other.village
        village.add(this)
    }
}
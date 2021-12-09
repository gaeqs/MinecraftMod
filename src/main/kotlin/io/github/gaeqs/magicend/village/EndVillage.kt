package io.github.gaeqs.magicend.village

import io.github.gaeqs.magicend.entity.EnderVillager
import io.github.gaeqs.magicend.entity.FarmerEnderman
import io.github.gaeqs.magicend.entity.GuardianEnderman
import io.github.gaeqs.magicend.entity.ShamanEnderman
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.dynamic.GlobalPos
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min
import kotlin.random.Random

class EndVillage {

    companion object {
        private val ID_GENERATOR = AtomicInteger()
    }

    val id = ID_GENERATOR.getAndIncrement()

    private val _villagers = mutableSetOf<EnderVillager>()
    val villagers: Set<EnderVillager> get() = _villagers.apply { if (removeIf { !it.isAlive }) callToPatrol() }
    val guardians get() = villagers.filterIsInstance<GuardianEnderman>()

    private var farmLuckMayBeActive = false
    private var farmLuckLastMillis: Long = 0
    val hasFarmLuck get() = farmLuckMayBeActive && farmLuckLastMillis + (1000 * 60 * 5) > System.currentTimeMillis()

    private val negativeScores = mutableMapOf<UUID, NegativeScore>()
    val publicEnemies get() = negativeScores.filter { it.value.currentScore > 50 }.keys

    private val occupiedFarmlands = mutableMapOf<GlobalPos, FarmerEnderman>()

    fun add(villager: EnderVillager): Boolean {
        if (_villagers.add(villager)) {
            callToPatrol()
            return true
        }
        return false
    }

    fun remove(villager: EnderVillager): Boolean {
        if (_villagers.remove(villager)) {
            callToPatrol()

            if (villager is FarmerEnderman) {
                occupiedFarmlands.values.removeIf { it == villager }
            }

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

    fun executeRitual(shaman: ShamanEnderman, block: BlockPos) {
        val villagers = villagers

        val farmerCount = villagers.count { it is FarmerEnderman }
        val guardianCount = villagers.count { it is GuardianEnderman }
        if (villagers.count() < 3 || farmerCount < 1 || guardianCount < 1) {
            executeSpawnRitual(shaman)
        }

        val particleType = when (Random.nextInt(3)) {
            0 -> {
                executeSpawnRitual(shaman)
                ParticleTypes.END_ROD
            }
            1 -> {
                executeStrengthRitual()
                ParticleTypes.FLAME
            }
            else -> {
                executeFarmRitual()
                ParticleTypes.HAPPY_VILLAGER
            }
        }

        val p = Vec3d.ofCenter(block)
        (shaman.world as ServerWorld).spawnParticles(
            particleType, p.x, p.y, p.z,
            20, 1.0, 1.0, 1.0, 0.0
        )
    }

    fun addNegativeScore(player: UUID, score: Int) {
        negativeScores.compute(player) { _, current ->
            NegativeScore((current?.currentScore ?: 0) + score, System.currentTimeMillis())
        }
    }

    fun isFarmlandOccupied(entity: FarmerEnderman, pos: GlobalPos): Boolean {
        val enderman = occupiedFarmlands[pos] ?: return false
        return entity != enderman && !enderman.isAlive
    }

    fun occupyFarmland(entity: FarmerEnderman, pos: GlobalPos) {
        occupiedFarmlands[pos] = entity
    }

    fun releaseFarmland(pos: GlobalPos) {
        occupiedFarmlands -= pos
    }

    private fun executeSpawnRitual(shaman: ShamanEnderman) {
        val villagers = villagers
        val farmerCount = villagers.count { it is FarmerEnderman }
        val guardianCount = villagers.count { it is GuardianEnderman }
        val shamanCount = villagers.count { it is ShamanEnderman }

        val entity = when (min(farmerCount, min(guardianCount, shamanCount))) {
            farmerCount -> FarmerEnderman.ENTITY_TYPE.create(shaman.world)
            guardianCount -> GuardianEnderman.ENTITY_TYPE.create(shaman.world)
            else -> ShamanEnderman.ENTITY_TYPE.create(shaman.world)
        }!!

        entity.copyPositionAndRotation(shaman)
        shaman.world.spawnEntity(entity)
    }

    private fun executeStrengthRitual() {
        guardians.forEach {
            it.addStatusEffect(StatusEffectInstance(StatusEffects.STRENGTH, 20 * 60 * 5, 1))
        }
    }

    private fun executeFarmRitual() {
        farmLuckMayBeActive = true
        farmLuckLastMillis = System.currentTimeMillis()
    }

    override fun toString() = "Village $id"
}
package io.github.gaeqs.magicend.util

import io.github.gaeqs.magicend.ai.EntityAI
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate

private val TARGET_CONDITIONS =
    TargetPredicate().setBaseMaxDistance(16.0).includeTeammates().ignoreEntityTargetRules()
private val TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING =
    TargetPredicate().setBaseMaxDistance(16.0).includeTeammates()
        .ignoreEntityTargetRules().ignoreDistanceScalingFactor()

fun LivingEntity.isEntityTargeteable(target: LivingEntity, ai: EntityAI? = null): Boolean {
    return if (ai?.hasMemory(MemoryTypes.ATTACK_TARGET) == true) {
        TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(this, target)
    } else {
        TARGET_CONDITIONS.test(this, target)
    }
}
package io.github.gaeqs.magicend.ai.defaults.memory

import io.github.gaeqs.magicend.ai.memory.MemoryType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.WalkTarget

object MemoryTypes {

    val NEARBY_LIVING_ENTITIES = MemoryType<List<LivingEntity>>("nearby_living_entities")
    val VISIBLE_NEARBY_LIVING_ENTITIES = MemoryType<List<LivingEntity>>("visible_nearby_living_entities")
    val ATTACK_TARGET = MemoryType<LivingEntity>("attack_target")
    val WALK_TARGET = MemoryType<WalkTarget>("walk_target")

}
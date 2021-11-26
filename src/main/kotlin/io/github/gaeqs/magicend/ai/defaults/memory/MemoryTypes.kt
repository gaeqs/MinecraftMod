package io.github.gaeqs.magicend.ai.defaults.memory

import io.github.gaeqs.magicend.ai.memory.MemoryType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.brain.WalkTarget
import net.minecraft.util.dynamic.GlobalPos

object MemoryTypes {

    val NEARBY_LIVING_ENTITIES = MemoryType<List<LivingEntity>>("nearby_living_entities")
    val VISIBLE_NEARBY_LIVING_ENTITIES = MemoryType<List<LivingEntity>>("visible_nearby_living_entities")
    val ATTACK_TARGET = MemoryType<LivingEntity>("attack_target")
    val ATTACK_IN_COOLDOWN = MemoryType<Boolean>("attack_in_cooldown")
    val WALK_TARGET = MemoryType<WalkTarget>("walk_target")
    val POINT_OF_INTEREST = MemoryType<GlobalPos>("point_of_interest")

}
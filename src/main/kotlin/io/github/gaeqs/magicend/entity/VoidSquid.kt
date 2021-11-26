package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.util.Identifier
import net.minecraft.world.World

class VoidSquid(type: EntityType<out VoidSquid>, world: World) : FlyingAIEntity(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_squid")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<VoidSquid> { type, world -> VoidSquid(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 0.8f)).build()

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
        }
    }

    init {
        initAI()
    }

    private fun initAI() {
    }

}
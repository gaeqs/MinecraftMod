package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.world.World

class VoidSquid(type: EntityType<out VoidSquid>, world: World) : FlyingAIEntity(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_squid")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<VoidSquid> { type, world -> VoidSquid(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 2.0f)).build()

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
        }
    }

    var shamanKills: Int = 0

    init {
        initAI()
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        shamanKills = nbt.getInt("shaman_kills")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("shaman_kills", shamanKills)
    }

    override fun onKilledOther(world: ServerWorld?, other: LivingEntity?) {
        super.onKilledOther(world, other)
        if (other is ShamanEnderman) shamanKills++
    }

    private fun initAI() {
    }

}
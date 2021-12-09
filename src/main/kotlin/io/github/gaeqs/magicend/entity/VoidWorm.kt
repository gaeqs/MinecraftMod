package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.*
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.SpawnEggItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import java.util.*

class VoidWorm(type: EntityType<out VoidWorm>, world: World) : AIEntity(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_worm")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.MONSTER,
            EntityType.EntityFactory<VoidWorm> { type, world -> VoidWorm(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 0.8f)).build()

        val EGG_ITEM_IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "void_worm_spawn_egg")
        val EGG_ITEM = SpawnEggItem(
            ENTITY_TYPE, 0x946794, 0xb93ec9,
            FabricItemSettings().group(ItemGroup.MISC)
        )

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0)
        }

        fun canSpawn(world: WorldAccess, spawnReason: SpawnReason, pos: BlockPos, random: Random): Boolean {
            return canMobSpawn(
                VoidSnake.ENTITY_TYPE,
                world,
                spawnReason,
                pos,
                random
            ) && world.difficulty != Difficulty.PEACEFUL
        }
    }

    var kills: Int = 0

    init {
        initAI()
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        kills = nbt.getInt("kills")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("kills", kills)
    }

    override fun onKilledOther(world: ServerWorld?, other: LivingEntity?) {
        super.onKilledOther(world, other)
        kills++
        if (kills >= 2) {
            evolve()
        }
    }

    override fun isDisallowedInPeaceful() = true

    private fun initAI() {
        ai.activities += TreeActivity("default", ai, rootLoopUnconditional {
            runAndWait {
                or {
                    and {
                        findNearestLivingEntities()
                        or {
                            findAttackTargetIfNotFound(32.0f) { it is EnderVillager }
                            findAttackTargetIfNotFound(32.0f) { it is PlayerEntity && !it.isCreative && !it.isSpectator }
                        }
                        walkToEntity(MemoryTypes.ATTACK_TARGET, 1.5f, 1.0f, 32.0f)
                        succeeder {
                            attack()
                        }
                    }
                    and {
                        findRandomWalkTarget(1.0f)
                        timed(40, 60) {
                            walkToTarget()
                        }
                    }
                }
            }
        })
    }

    private fun evolve() {
        if (removed) return
        method_29243(VoidSnake.ENTITY_TYPE, true)
    }

}
package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.defaults.tree.findPointOfInterest
import io.github.gaeqs.magicend.ai.defaults.tree.walkToPosition
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.and
import io.github.gaeqs.magicend.ai.tree.node.or
import io.github.gaeqs.magicend.ai.tree.node.rootLoopUnconditional
import io.github.gaeqs.magicend.ai.tree.node.wait
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.world.World

class ShamanEnderman(type: EntityType<out ShamanEnderman>, world: World) : EnderVillager(type, world) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "shaman_enderman")
        val ENTITY_TYPE = FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<ShamanEnderman> { type, world -> ShamanEnderman(type, world) }
        ).dimensions(EntityDimensions.fixed(0.8f, 3.0f)).build()

        fun createExampleEntityAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20000000298023224)
        }
    }

    var sacrifices: Int = 0

    init {
        initAI()
    }


    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        sacrifices = nbt.getInt("sacrifices")
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt("sacrifices", sacrifices)
    }

    private fun initAI() {
        ai.activities += TreeActivity("idle", ai,
            rootLoopUnconditional {
                or {
                    and {
                        findPointOfInterest(PointOfInterestTypes.DRAGON_STATUE, MemoryTypes.POINT_OF_INTEREST, 48)
                        walkToPosition(MemoryTypes.POINT_OF_INTEREST, 2.0f)
                        // Position reached successfully. Waiting.
                        wait(50)
                    }
                    // Walk failed! Just in case skip a tick.
                    wait(1)
                }
            }
        )
    }

}
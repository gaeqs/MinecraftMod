package io.github.gaeqs.magicend.block

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.entity.EnderVillager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.block.CropBlock
import net.minecraft.block.Material
import net.minecraft.block.ShapeContext
import net.minecraft.item.*
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import java.util.*

class ChorusWheat : CropBlock(
    Settings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)
) {


    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "chorus_wheat")
        val BLOCK = ChorusWheat()
        val WHEAT_ITEM = Item(FabricItemSettings().group(ItemGroup.MATERIALS))

        val SEEDS_IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "chorus_seeds")
        val SEEDS_ITEM = AliasedBlockItem(BLOCK, FabricItemSettings().group(ItemGroup.MATERIALS))

        val BREAD_IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "chorus_bread")
        val BREAD_ITEM = Item(FabricItemSettings().group(ItemGroup.FOOD).food(FoodComponents.BREAD))


        private val AGE_TO_SHAPE = arrayOf(
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            createCuboidShape(0.0, 0.0, 0.0, 16.0, 9.0, 16.0)
        )
    }

    @Environment(EnvType.CLIENT)
    override fun getSeedsItem(): ItemConvertible {
        return SEEDS_ITEM
    }

    override fun randomTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            val i = getAge(state)

            if (i < this.maxAge) {
                val lucky = hasLuck(world, pos)
                val f = getAvailableMoisture(this, world, pos).run { if (lucky) this * 8 else this }
                if (random.nextInt((25.0f / f).toInt() + 1) == 0) {
                    world.setBlockState(pos, withAge(i + 1), 2)

                    if (lucky) {
                        val p = Vec3d.ofCenter(pos)
                        world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, p.x, p.y, p.z, 20, 0.5, 0.5, 0.5, 0.0)
                    }
                }
            }
        }
    }

    private fun hasLuck(world: ServerWorld, pos: BlockPos): Boolean {
        val box = Box(pos).expand(48.0)
        return world.getEntitiesByClass(EnderVillager::class.java, box) { it.village.hasFarmLuck }.isNotEmpty()
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape = AGE_TO_SHAPE[state.get(ageProperty)]

}
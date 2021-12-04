package io.github.gaeqs.magicend.block

import io.github.gaeqs.magicend.MinecraftMod
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.*
import net.minecraft.item.*
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView

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
    override fun getSeedsItem(): ItemConvertible? {
        return SEEDS_ITEM
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape = AGE_TO_SHAPE[state.get(ageProperty)]

}
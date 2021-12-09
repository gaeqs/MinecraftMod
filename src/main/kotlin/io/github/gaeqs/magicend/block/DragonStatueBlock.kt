package io.github.gaeqs.magicend.block

import io.github.gaeqs.magicend.MinecraftMod
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.ShapeContext
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class DragonStatueBlock : Block(FabricBlockSettings.of(Material.STONE).nonOpaque().strength(2.0f)) {


    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "dragon_statue")
        val BLOCK = DragonStatueBlock()
        val BLOCK_ITEM = BlockItem(BLOCK, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS))
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape {
        return VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(EndWindowBlock.FACING, rotation.rotate(state.get(EndWindowBlock.FACING)) as Direction)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(EndWindowBlock.FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return super.getPlacementState(ctx)?.with(EndWindowBlock.FACING, ctx.playerFacing.rotateYClockwise())
    }


}
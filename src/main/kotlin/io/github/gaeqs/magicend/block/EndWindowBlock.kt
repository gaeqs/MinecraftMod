package io.github.gaeqs.magicend.block

import io.github.gaeqs.magicend.MinecraftMod
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.*
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView

class EndWindowBlock : Block(FabricBlockSettings.of(Material.METAL).strength(2.0f)) {

    companion object {
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "end_window")
        val BLOCK = EndWindowBlock()
        val BLOCK_ITEM = BlockItem(BLOCK, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS))
    }

    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape {
        val facing = state.get(FACING)
        return if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            VoxelShapes.cuboid(0.4375, 0.0, 0.0, 0.5625, 1.0, 1.0)
        } else {
            VoxelShapes.cuboid(0.0, 0.0, 0.4375, 1.0, 1.0, 0.5625)
        }
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, rotation.rotate(state.get(FACING)) as Direction)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        println(ctx.playerFacing.rotateYClockwise())
        return super.getPlacementState(ctx)?.with(FACING, ctx.playerFacing.rotateYClockwise())
    }


}
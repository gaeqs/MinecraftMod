package io.github.gaeqs.magicend.block

import io.github.gaeqs.magicend.MinecraftMod
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.*
import net.minecraft.fluid.Fluids
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.tag.BlockTags
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class EndFenceBlock :
    HorizontalConnectingBlock(
        0.0f,
        0.0f,
        0.0f,
        0.0f,
        0.0f,
        FabricBlockSettings.of(Material.METAL).strength(2.0f)
    ) {

    companion object {
        val FACING: DirectionProperty = HorizontalFacingBlock.FACING
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "end_fence")
        val BLOCK = EndFenceBlock()
        val BLOCK_ITEM = BlockItem(BLOCK, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS))


    }

    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.NORTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        val blockView: BlockView = ctx.world
        val pos = ctx.blockPos
        val fluidState = ctx.world.getFluidState(ctx.blockPos)
        val nPos = pos.north()
        val ePos = pos.east()
        val sPos = pos.south()
        val wPos = pos.west()
        val nBlock = blockView.getBlockState(nPos)
        val eBlock = blockView.getBlockState(ePos)
        val sBlock = blockView.getBlockState(sPos)
        val wBlock = blockView.getBlockState(wPos)

        return getPlacementState(ctx)
            ?.with(
                NORTH,
                canConnect(nBlock, nBlock.isSideSolidFullSquare(blockView, nPos, Direction.SOUTH), Direction.SOUTH)
            )
            ?.with(
                EAST,
                canConnect(eBlock, eBlock.isSideSolidFullSquare(blockView, ePos, Direction.WEST), Direction.WEST)
            )
            ?.with(
                SOUTH,
                canConnect(sBlock, sBlock.isSideSolidFullSquare(blockView, sPos, Direction.NORTH), Direction.NORTH)
            )
            ?.with(
                WEST,
                canConnect(wBlock, wBlock.isSideSolidFullSquare(blockView, wPos, Direction.EAST), Direction.EAST)
            )
            ?.with(WATERLOGGED, fluidState.fluid == Fluids.WATER)
    }

    override fun getStateForNeighborUpdate(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        world: WorldAccess,
        pos: BlockPos?,
        neighborPos: BlockPos?
    ): BlockState? {
        if (state.get(WATERLOGGED) as Boolean) {
            world.fluidTickScheduler.schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world))
        }
        return if (direction.axis.type == Direction.Type.HORIZONTAL) {
            val op = direction.opposite
            val isSideFullSquare = neighborState.isSideSolidFullSquare(world, neighborPos, op)
            state.with(FACING_PROPERTIES[direction], canConnect(neighborState, isSideFullSquare, op))
        } else {
            super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos)
        }
    }

    private fun canConnect(state: BlockState, neighborIsFullSquare: Boolean, dir: Direction?): Boolean {
        val block = state.block
        val bl = isFence(block)
        val bl2 = block is FenceGateBlock && FenceGateBlock.canWallConnect(state, dir)
        return !cannotConnect(block) && neighborIsFullSquare || bl || bl2
    }


    private fun isFence(block: Block): Boolean {
        return block.isIn(BlockTags.FENCES)
                && block.isIn(BlockTags.WOODEN_FENCES) == defaultState.isIn(BlockTags.WOODEN_FENCES)
    }

}
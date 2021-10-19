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
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.tag.BlockTags
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess

class EndFenceBlock :
    HorizontalConnectingBlock(
        1.0f,
        1.0f,
        16.0f,
        16.0f,
        24.0f,
        FabricBlockSettings.of(Material.METAL).strength(2.0f)
    ) {

    companion object {
        val UP: BooleanProperty = Properties.UP
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "end_fence")
        val PILAR_BOUNDING_BOX = VoxelShapes.cuboid(0.25, 0.0, 0.25, 0.75, 1.0, 0.75)
        val PILAR_COLLISION_BOX = VoxelShapes.cuboid(0.25, 0.0, 0.25, 0.75, 1.5, 0.75)
        val BLOCK = EndFenceBlock()
        val BLOCK_ITEM = BlockItem(BLOCK, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS))
    }


    private val boundingShapesWithPilar = boundingShapes.map { VoxelShapes.union(it, PILAR_BOUNDING_BOX) }
    private val collisionShapesWithPilar = collisionShapes.map { VoxelShapes.union(it, PILAR_COLLISION_BOX) }

    init {
        defaultState = stateManager.defaultState
            .with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(UP, false)
            .with(WATERLOGGED, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(NORTH, EAST, WEST, SOUTH, UP, WATERLOGGED)
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

        val state = super.getPlacementState(ctx)
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

        return checkPilarState(state, pos)
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
            val result = state.with(FACING_PROPERTIES[direction], canConnect(neighborState, isSideFullSquare, op))
            checkPilarState(result, pos)
        } else {
            super.getStateForNeighborUpdate(checkPilarState(state, pos), direction, neighborState, world, pos, neighborPos)
        }
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return if (state == null || !state.get(UP)) super.getOutlineShape(state, world, pos, context)
        else boundingShapesWithPilar[getShapeIndex(state)]
    }


    override fun getCollisionShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        return if (state == null || !state.get(UP)) super.getCollisionShape(state, world, pos, context)
        else collisionShapesWithPilar[getShapeIndex(state)]
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

    private fun checkPilarState(state: BlockState?, pos: BlockPos?): BlockState? {
        if (state == null) return null
        val list = mutableListOf(NORTH, SOUTH, EAST, WEST).filter { state.get(it) }

        return if (list.size == 2) {
            val line = NORTH in list && SOUTH in list || EAST in list && WEST in list
            state.with(UP, !line || pos != null && (pos.x + pos.z) % 3 == 0)
        } else {
            state.with(UP, true)
        }
    }

}
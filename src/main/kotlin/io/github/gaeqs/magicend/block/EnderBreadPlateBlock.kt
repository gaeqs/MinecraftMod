package io.github.gaeqs.magicend.block

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.block.entity.EnderBreadPlateBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class EnderBreadPlateBlock : Block(FabricBlockSettings.of(Material.STONE).nonOpaque().strength(2.0f)), BlockEntityProvider {


    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "ender_bread_plate")
        val BLOCK = EnderBreadPlateBlock()
        val BLOCK_ITEM = BlockItem(BLOCK, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS))
        val BLOCK_ENTITY = BlockEntityType.Builder.create({ EnderBreadPlateBlockEntity() }, BLOCK).build(null)
    }

    override fun createBlockEntity(world: BlockView?) = EnderBreadPlateBlockEntity()

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape {
        return VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 7.0 / 16.0, 1.0)
    }

}
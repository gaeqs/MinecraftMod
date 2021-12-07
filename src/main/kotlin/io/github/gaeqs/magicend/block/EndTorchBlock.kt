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
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.*

class EndTorchBlock : Block(FabricBlockSettings.of(Material.STONE).strength(2.0f)) {


    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "end_torch")
        val BLOCK = EndTorchBlock()
        val BLOCK_ITEM = BlockItem(BLOCK, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS))
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape {
        return VoxelShapes.cuboid(0.35, 0.0, 0.35, 0.65, 1.0, 0.65)
    }


    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        for (i in 0..1) {
            var d = pos.x.toDouble() + 0.4 + (random.nextDouble()/5)
            val e = pos.y.toDouble() + 0.4
            var f = pos.z.toDouble() + 0.4 + (random.nextDouble()/5)
            var g = 0.01
            val h = 0.3
            var j = 0.01

            world.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j)
        }
    }


}
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

class EnderCoreBlock : Block(FabricBlockSettings.of(Material.STONE).strength(2.0f)) {


    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "ender_core")
        val BLOCK = EnderCoreBlock()
        val BLOCK_ITEM = BlockItem(BLOCK, FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS))
    }

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape {
        return VoxelShapes.cuboid(0.1, 0.0, 0.1, 0.9, 0.8, 0.9)
    }


    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        for (i in 0..3) {
            var d = pos.x.toDouble() + random.nextDouble()
            val e = pos.y.toDouble() + random.nextDouble()
            var f = pos.z.toDouble() + random.nextDouble()
            var g = (random.nextFloat().toDouble() - 0.5) * 0.5
            val h = (random.nextFloat().toDouble() - 0.5) * 0.5
            var j = (random.nextFloat().toDouble() - 0.5) * 0.5
            val k = random.nextInt(2) * 2 - 1
            if (!world.getBlockState(pos.west()).isOf(this) && !world.getBlockState(pos.east()).isOf(this)) {
                d = pos.x.toDouble() + 0.5 + 0.25 * k.toDouble()
                g = (random.nextFloat() * 2.0f * k.toFloat()).toDouble()
            } else {
                f = pos.z.toDouble() + 0.5 + 0.25 * k.toDouble()
                j = (random.nextFloat() * 2.0f * k.toFloat()).toDouble()
            }
            world.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j)
        }
    }


}
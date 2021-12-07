package io.github.gaeqs.magicend.block

import io.github.gaeqs.magicend.MinecraftMod
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.TallPlantBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

class DummyBlock : TallPlantBlock(FabricBlockSettings.of(Material.WOOD)
    .nonOpaque().strength(2.0f)) {

    companion object {
        val IDENTIFIER = Identifier(MinecraftMod.MOD_ID, "dummy")
        val BLOCK = DummyBlock()
        val BLOCK_ITEM = BlockItem(BLOCK, FabricItemSettings().group(ItemGroup.DECORATIONS))
    }

    override fun canPlantOnTop(floor: BlockState?, world: BlockView?, pos: BlockPos?): Boolean {
        return true
    }

}
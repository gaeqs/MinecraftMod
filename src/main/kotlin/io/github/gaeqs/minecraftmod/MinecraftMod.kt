package io.github.gaeqs.minecraftmod

import io.github.gaeqs.minecraftmod.entity.ExampleEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.ShapeContext
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.*

class ExampleBlock : Block(FabricBlockSettings.of(Material.METAL).strength(0.4f)) {

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape {
        return VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.8, 1.0)
    }

}

class EnderWindowBlock : Block(FabricBlockSettings.of(Material.METAL).strength(0.4f)) {

    override fun getOutlineShape(state: BlockState, world: BlockView, pos: BlockPos, context: ShapeContext)
            : VoxelShape {
        return VoxelShapes.cuboid(0.0, 0.0, 0.3, 1.0, 1.0, 0.7)
    }


}

class EnderCoreBlock : Block(FabricBlockSettings.of(Material.METAL).strength(0.4f)) {

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

object MinecraftMod : ModInitializer {

    const val MOD_ID = "minecraft_mod"
    val EMPTY_IDENTIFIER = Identifier(MOD_ID, "empty")

    val EXAMPLE_BLOCK = EnderWindowBlock()
    val EXAMPLE_BLOCK_ITEM = BlockItem(EXAMPLE_BLOCK, FabricItemSettings().group(ItemGroup.MISC))

    val ENDER_CORE = EnderCoreBlock()
    val ENDER_CORE_ITEM = BlockItem(ENDER_CORE, FabricItemSettings().group(ItemGroup.MISC))

    val ENDER_WINDOW = ExampleBlock()
    val ENDER_WINDOW_ITEM = BlockItem(ENDER_WINDOW, FabricItemSettings().group(ItemGroup.MISC))

    val EXAMPLE_ENTITY = Registry.register(Registry.ENTITY_TYPE, Identifier(MOD_ID, "example"),
        FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<ExampleEntity> { type, world -> ExampleEntity(type, world) }
        ).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    )

    override fun onInitialize() {
        println("Example mod has been initialized.")
        Registry.register(Registry.BLOCK, Identifier(MOD_ID, "example_block"), EXAMPLE_BLOCK)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "example_block"), EXAMPLE_BLOCK_ITEM)
        Registry.register(Registry.BLOCK, Identifier(MOD_ID, "ender_core"), ENDER_CORE)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "ender_core"), ENDER_CORE_ITEM)
        Registry.register(Registry.BLOCK, Identifier(MOD_ID, "ender_window"), ENDER_WINDOW)
        Registry.register(Registry.ITEM, Identifier(MOD_ID, "ender_window"), ENDER_WINDOW_ITEM)
        registerExampleEntity()
    }


    private fun registerExampleEntity() {

        FabricDefaultAttributeRegistry.register(EXAMPLE_ENTITY, ExampleEntity.createExampleEntityAttributes())
    }
}
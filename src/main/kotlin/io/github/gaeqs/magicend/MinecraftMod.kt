package io.github.gaeqs.magicend

import io.github.gaeqs.magicend.block.*
/*import io.github.gaeqs.magicend.block.EndWindowBlock
import io.github.gaeqs.magicend.block.EnderCoreBlock*/
import io.github.gaeqs.magicend.entity.ExampleEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object MinecraftMod : ModInitializer {

    const val MOD_ID = "magic_end"

    val EXAMPLE_ENTITY = Registry.register(Registry.ENTITY_TYPE, Identifier(MOD_ID, "example"),
        FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            EntityType.EntityFactory<ExampleEntity> { type, world -> ExampleEntity(type, world) }
        ).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    )

    override fun onInitialize() {
        Registry.register(Registry.BLOCK, EnderCoreBlock.IDENTIFIER, EnderCoreBlock.BLOCK)
        Registry.register(Registry.ITEM, EnderCoreBlock.IDENTIFIER, EnderCoreBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, EndTorch.IDENTIFIER, EndTorch.BLOCK)
        Registry.register(Registry.ITEM, EndTorch.IDENTIFIER, EndTorch.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, EndWindowBlock.IDENTIFIER, EndWindowBlock.BLOCK)
        Registry.register(Registry.ITEM, EndWindowBlock.IDENTIFIER, EndWindowBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, ObsidianBricksBlock.IDENTIFIER, ObsidianBricksBlock.BLOCK)
        Registry.register(Registry.ITEM, ObsidianBricksBlock.IDENTIFIER, ObsidianBricksBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, EndFenceBlock.IDENTIFIER, EndFenceBlock.BLOCK)
        Registry.register(Registry.ITEM, EndFenceBlock.IDENTIFIER, EndFenceBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, EndFenceTopBlock.IDENTIFIER, EndFenceTopBlock.BLOCK)
        Registry.register(Registry.ITEM, EndFenceTopBlock.IDENTIFIER, EndFenceTopBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, EndFenceCornerBlock.IDENTIFIER, EndFenceCornerBlock.BLOCK)
        Registry.register(Registry.ITEM, EndFenceCornerBlock.IDENTIFIER, EndFenceCornerBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, EndFenceCrossBlock.IDENTIFIER, EndFenceCrossBlock.BLOCK)
        Registry.register(Registry.ITEM, EndFenceCrossBlock.IDENTIFIER, EndFenceCrossBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, EndFenceEndBlock.IDENTIFIER, EndFenceEndBlock.BLOCK)
        Registry.register(Registry.ITEM, EndFenceEndBlock.IDENTIFIER, EndFenceEndBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, EndFenceStraightBlock.IDENTIFIER, EndFenceStraightBlock.BLOCK)
        Registry.register(Registry.ITEM, EndFenceStraightBlock.IDENTIFIER, EndFenceStraightBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, EndFenceTBlock.IDENTIFIER, EndFenceTBlock.BLOCK)
        Registry.register(Registry.ITEM, EndFenceTBlock.IDENTIFIER, EndFenceTBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, DragonStatueBlock.IDENTIFIER, DragonStatueBlock.BLOCK)
        Registry.register(Registry.ITEM, DragonStatueBlock.IDENTIFIER, DragonStatueBlock.BLOCK_ITEM)
        registerExampleEntity()
    }


    private fun registerExampleEntity() {

        FabricDefaultAttributeRegistry.register(EXAMPLE_ENTITY, ExampleEntity.createExampleEntityAttributes())
    }
}
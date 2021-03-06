package io.github.gaeqs.magicend

import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.block.*
import io.github.gaeqs.magicend.entity.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.mixin.`object`.builder.SpawnRestrictionAccessor
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.SpawnRestriction
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.SpawnSettings
import java.util.function.Consumer
import java.util.function.Predicate

object MinecraftMod : ModInitializer {

    const val MOD_ID = "magic_end"
    val VOID_SHARD_IDENTIFIER = Identifier(MOD_ID, "void_shard")
    val VOID_SHARD_ITEM = Item(FabricItemSettings().group(ItemGroup.MISC))

    override fun onInitialize() {
        Registry.register(Registry.BLOCK, EnderCoreBlock.IDENTIFIER, EnderCoreBlock.BLOCK)
        Registry.register(Registry.ITEM, EnderCoreBlock.IDENTIFIER, EnderCoreBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, EndTorchBlock.IDENTIFIER, EndTorchBlock.BLOCK)
        Registry.register(Registry.ITEM, EndTorchBlock.IDENTIFIER, EndTorchBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, EndWindowBlock.IDENTIFIER, EndWindowBlock.BLOCK)
        Registry.register(Registry.ITEM, EndWindowBlock.IDENTIFIER, EndWindowBlock.BLOCK_ITEM)
        Registry.register(Registry.BLOCK, ObsidianBricksBlock.IDENTIFIER, ObsidianBricksBlock.BLOCK)
        Registry.register(Registry.ITEM, ObsidianBricksBlock.IDENTIFIER, ObsidianBricksBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, EndFenceBlock.IDENTIFIER, EndFenceBlock.BLOCK)
        Registry.register(Registry.ITEM, EndFenceBlock.IDENTIFIER, EndFenceBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, DragonStatueBlock.IDENTIFIER, DragonStatueBlock.BLOCK)
        Registry.register(Registry.ITEM, DragonStatueBlock.IDENTIFIER, DragonStatueBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, EnderBreadPlateBlock.IDENTIFIER, EnderBreadPlateBlock.BLOCK)
        Registry.register(Registry.ITEM, EnderBreadPlateBlock.IDENTIFIER, EnderBreadPlateBlock.BLOCK_ITEM)
        Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            EnderBreadPlateBlock.IDENTIFIER,
            EnderBreadPlateBlock.BLOCK_ENTITY
        )

        Registry.register(Registry.BLOCK, EnderTableBlock.IDENTIFIER, EnderTableBlock.BLOCK)
        Registry.register(Registry.ITEM, EnderTableBlock.IDENTIFIER, EnderTableBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, DummyBlock.IDENTIFIER, DummyBlock.BLOCK)
        Registry.register(Registry.ITEM, DummyBlock.IDENTIFIER, DummyBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, ChorusWheatBlock.IDENTIFIER, ChorusWheatBlock.BLOCK)
        Registry.register(Registry.ITEM, ChorusWheatBlock.IDENTIFIER, ChorusWheatBlock.WHEAT_ITEM)
        Registry.register(Registry.ITEM, ChorusWheatBlock.SEEDS_IDENTIFIER, ChorusWheatBlock.SEEDS_ITEM)
        Registry.register(Registry.ITEM, ChorusWheatBlock.BREAD_IDENTIFIER, ChorusWheatBlock.BREAD_ITEM)

        Registry.register(Registry.ITEM, VOID_SHARD_IDENTIFIER, VOID_SHARD_ITEM)

        registerEntities()
        initializeSpawns()
        PointOfInterestTypes.init()
    }


    private fun registerEntities() {
        Registry.register(Registry.ENTITY_TYPE, ShamanEnderman.IDENTIFIER, ShamanEnderman.ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(
            ShamanEnderman.ENTITY_TYPE,
            ShamanEnderman.createExampleEntityAttributes()
        )

        Registry.register(Registry.ENTITY_TYPE, FarmerEnderman.IDENTIFIER, FarmerEnderman.ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(
            FarmerEnderman.ENTITY_TYPE,
            FarmerEnderman.createExampleEntityAttributes()
        )

        Registry.register(Registry.ENTITY_TYPE, GuardianEnderman.IDENTIFIER, GuardianEnderman.ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(
            GuardianEnderman.ENTITY_TYPE,
            GuardianEnderman.createExampleEntityAttributes()
        )

        Registry.register(Registry.ENTITY_TYPE, VoidSnake.IDENTIFIER, VoidSnake.ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(
            VoidSnake.ENTITY_TYPE,
            VoidSnake.createExampleEntityAttributes()
        )

        Registry.register(Registry.ENTITY_TYPE, VoidWorm.IDENTIFIER, VoidWorm.ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(
            VoidWorm.ENTITY_TYPE,
            VoidWorm.createExampleEntityAttributes()
        )

        Registry.register(Registry.ENTITY_TYPE, VoidSquid.IDENTIFIER, VoidSquid.ENTITY_TYPE)
        FabricDefaultAttributeRegistry.register(
            VoidSquid.ENTITY_TYPE,
            VoidSquid.createExampleEntityAttributes()
        )

        Registry.register(Registry.ITEM, ShamanEnderman.EGG_ITEM_IDENTIFIER, ShamanEnderman.EGG_ITEM)
        Registry.register(Registry.ITEM, GuardianEnderman.EGG_ITEM_IDENTIFIER, GuardianEnderman.EGG_ITEM)
        Registry.register(Registry.ITEM, FarmerEnderman.EGG_ITEM_IDENTIFIER, FarmerEnderman.EGG_ITEM)
        Registry.register(Registry.ITEM, VoidWorm.EGG_ITEM_IDENTIFIER, VoidWorm.EGG_ITEM)
        Registry.register(Registry.ITEM, VoidSnake.EGG_ITEM_IDENTIFIER, VoidSnake.EGG_ITEM)
        Registry.register(Registry.ITEM, VoidSquid.EGG_ITEM_IDENTIFIER, VoidSquid.EGG_ITEM)
    }

    private fun initializeSpawns() {
        val endSelector = Predicate<BiomeSelectionContext> {
            it.biome.category == Biome.Category.THEEND
        }

        BiomeModifications.create(VoidWorm.IDENTIFIER).add(ModificationPhase.ADDITIONS, endSelector, Consumer {
            it.spawnSettings.addSpawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(VoidWorm.ENTITY_TYPE, 3, 2, 4))
        })

        BiomeModifications.create(VoidSquid.IDENTIFIER).add(ModificationPhase.ADDITIONS, endSelector, Consumer {
            it.spawnSettings.addSpawn(SpawnGroup.MONSTER, SpawnSettings.SpawnEntry(VoidSquid.ENTITY_TYPE, 2, 2, 4))
        })


        SpawnRestrictionAccessor.callRegister(
            VoidWorm.ENTITY_TYPE,
            SpawnRestriction.Location.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES
        ) { _, wAccess, reason, pos, random -> VoidWorm.canSpawn(wAccess, reason, pos, random) }

        SpawnRestrictionAccessor.callRegister(
            VoidSquid.ENTITY_TYPE,
            SpawnRestriction.Location.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES
        ) { _, wAccess, reason, pos, random -> VoidSquid.canSpawn(wAccess, reason, pos, random) }


    }
}
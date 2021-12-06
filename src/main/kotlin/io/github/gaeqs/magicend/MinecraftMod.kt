package io.github.gaeqs.magicend

/*import io.github.gaeqs.magicend.block.EndWindowBlock
import io.github.gaeqs.magicend.block.EnderCoreBlock*/
import io.github.gaeqs.magicend.ai.defaults.PointOfInterestTypes
import io.github.gaeqs.magicend.block.*
import io.github.gaeqs.magicend.entity.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext
import net.fabricmc.fabric.api.biome.v1.ModificationPhase
import net.fabricmc.fabric.mixin.`object`.builder.SpawnRestrictionAccessor
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.SpawnRestriction
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.SpawnSettings
import java.util.function.Consumer
import java.util.function.Predicate

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

        Registry.register(Registry.BLOCK, DragonStatueBlock.IDENTIFIER, DragonStatueBlock.BLOCK)
        Registry.register(Registry.ITEM, DragonStatueBlock.IDENTIFIER, DragonStatueBlock.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, EnderBreadPlateBlock.IDENTIFIER, EnderBreadPlateBlock.BLOCK)
        Registry.register(Registry.ITEM, EnderBreadPlateBlock.IDENTIFIER, EnderBreadPlateBlock.BLOCK_ITEM)
        Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            EnderBreadPlateBlock.IDENTIFIER,
            EnderBreadPlateBlock.BLOCK_ENTITY
        )

        Registry.register(Registry.BLOCK, EnderTable.IDENTIFIER, EnderTable.BLOCK)
        Registry.register(Registry.ITEM, EnderTable.IDENTIFIER, EnderTable.BLOCK_ITEM)

        Registry.register(Registry.BLOCK, ChorusWheat.IDENTIFIER, ChorusWheat.BLOCK)
        Registry.register(Registry.ITEM, ChorusWheat.IDENTIFIER, ChorusWheat.WHEAT_ITEM)
        Registry.register(Registry.ITEM, ChorusWheat.SEEDS_IDENTIFIER, ChorusWheat.SEEDS_ITEM)
        Registry.register(Registry.ITEM, ChorusWheat.BREAD_IDENTIFIER, ChorusWheat.BREAD_ITEM)

        registerEntities()
        initializeSpawns()
        PointOfInterestTypes.init()
    }


    private fun registerEntities() {
        FabricDefaultAttributeRegistry.register(EXAMPLE_ENTITY, ExampleEntity.createExampleEntityAttributes())

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
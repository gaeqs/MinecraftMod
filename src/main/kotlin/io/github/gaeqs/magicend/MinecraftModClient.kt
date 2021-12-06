package io.github.gaeqs.magicend

import io.github.gaeqs.magicend.block.EnderBreadPlateBlock
import io.github.gaeqs.magicend.block.renderer.EnderBreadPlateBlockEntityRenderer
import io.github.gaeqs.magicend.entity.*
import io.github.gaeqs.magicend.entity.render.*
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry

object MinecraftModClient : ClientModInitializer {

    override fun onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(FarmerEnderman.ENTITY_TYPE)
        { dispatcher, _ -> FarmerEndermanRenderer(dispatcher) }
        EntityRendererRegistry.INSTANCE.register(GuardianEnderman.ENTITY_TYPE)
        { dispatcher, _ -> GuardianEndermanRenderer(dispatcher) }
        EntityRendererRegistry.INSTANCE.register(ShamanEnderman.ENTITY_TYPE)
        { dispatcher, _ -> ShamanEndermanRenderer(dispatcher) }
        EntityRendererRegistry.INSTANCE.register(VoidSnake.ENTITY_TYPE)
        { dispatcher, _ -> VoidSnakeRenderer(dispatcher) }
        EntityRendererRegistry.INSTANCE.register(VoidWorm.ENTITY_TYPE)
        { dispatcher, _ -> VoidWormRenderer(dispatcher) }
        EntityRendererRegistry.INSTANCE.register(VoidSquid.ENTITY_TYPE)
        { dispatcher, _ -> VoidSquidRenderer(dispatcher) }

        BlockEntityRendererRegistry.INSTANCE.register(EnderBreadPlateBlock.BLOCK_ENTITY)
        { EnderBreadPlateBlockEntityRenderer(it) }
    }

}
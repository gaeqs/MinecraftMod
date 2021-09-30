package io.github.gaeqs.magicend

import io.github.gaeqs.magicend.entity.render.ExampleEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry

object MinecraftModClient : ClientModInitializer {

    override fun onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(MinecraftMod.EXAMPLE_ENTITY)
        { dispatcher, _ -> ExampleEntityRenderer(dispatcher) }
    }

}
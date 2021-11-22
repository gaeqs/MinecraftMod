package io.github.gaeqs.magicend

import io.github.gaeqs.magicend.entity.FarmerEnderman
import io.github.gaeqs.magicend.entity.GuardianEnderman
import io.github.gaeqs.magicend.entity.ShamanEnderman
import io.github.gaeqs.magicend.entity.render.ExampleEntityRenderer
import io.github.gaeqs.magicend.entity.render.FarmerEndermanRenderer
import io.github.gaeqs.magicend.entity.render.GuardianEndermanRenderer
import io.github.gaeqs.magicend.entity.render.ShamanEndermanRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry

object MinecraftModClient : ClientModInitializer {

    override fun onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(MinecraftMod.EXAMPLE_ENTITY)
        { dispatcher, _ -> ExampleEntityRenderer(dispatcher) }

        EntityRendererRegistry.INSTANCE.register(FarmerEnderman.ENTITY_TYPE)
        { dispatcher, _ -> FarmerEndermanRenderer(dispatcher) }
        EntityRendererRegistry.INSTANCE.register(GuardianEnderman.ENTITY_TYPE)
        { dispatcher, _ -> GuardianEndermanRenderer(dispatcher) }
        EntityRendererRegistry.INSTANCE.register(ShamanEnderman.ENTITY_TYPE)
        { dispatcher, _ -> ShamanEndermanRenderer(dispatcher) }
    }

}
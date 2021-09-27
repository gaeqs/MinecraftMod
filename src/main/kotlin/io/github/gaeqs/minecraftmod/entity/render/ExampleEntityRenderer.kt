package io.github.gaeqs.minecraftmod.entity.render

import io.github.gaeqs.minecraftmod.MinecraftMod
import io.github.gaeqs.minecraftmod.entity.ExampleEntity
import io.github.gaeqs.minecraftmod.entity.render.feature.TopTextureFeature
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier

class ExampleEntityRenderer(context: EntityRenderDispatcher) :
    MobEntityRenderer<ExampleEntity, ExampleEntityModel<ExampleEntity>>(context, ExampleEntityModel(), 0.3f) {

    init {
        addFeature(TopTextureFeature(this, Math.PI.toFloat() / 20.0f, 0.2f))
    }

    override fun getTexture(entity: ExampleEntity): Identifier {
        return Identifier(MinecraftMod.MOD_ID, "textures/entity/example/example.png")
    }
}
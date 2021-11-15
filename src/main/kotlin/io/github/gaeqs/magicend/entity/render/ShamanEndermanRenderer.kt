package io.github.gaeqs.magicend.entity.render

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.entity.ShamanEnderman
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer
import net.minecraft.util.Identifier

class ShamanEndermanRenderer(context: EntityRenderDispatcher) :
    MobEntityRenderer<ShamanEnderman, ShamanEndermanModel<ShamanEnderman>>(context, ShamanEndermanModel(), 0.3f) {

    init {
        addFeature(object : EyesFeatureRenderer<ShamanEnderman, ShamanEndermanModel<ShamanEnderman>>(this) {
            private val SKIN =
                RenderLayer.getEyes(Identifier(MinecraftMod.MOD_ID, "textures/entity/shaman_enderman/eyes.png"))

            override fun getEyesTexture() = SKIN
        })
    }

    override fun getTexture(entity: ShamanEnderman): Identifier {
        return Identifier(MinecraftMod.MOD_ID, "textures/entity/shaman_enderman/main.png")
    }
}
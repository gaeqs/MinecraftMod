package io.github.gaeqs.magicend.entity.render

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.entity.FarmerEnderman
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer
import net.minecraft.util.Identifier

class FarmerEndermanRenderer(context: EntityRenderDispatcher) :
    MobEntityRenderer<FarmerEnderman, FarmerEndermanModel<FarmerEnderman>>(context, FarmerEndermanModel(), 0.3f) {

    init {
        addFeature(object : EyesFeatureRenderer<FarmerEnderman, FarmerEndermanModel<FarmerEnderman>>(this) {
            private val SKIN =
                RenderLayer.getEyes(Identifier(MinecraftMod.MOD_ID, "textures/entity/farmer_enderman/eyes.png"))

            override fun getEyesTexture() = SKIN
        })
    }

    override fun getTexture(entity: FarmerEnderman): Identifier {
        return Identifier(MinecraftMod.MOD_ID, "textures/entity/farmer_enderman/main.png")
    }
}
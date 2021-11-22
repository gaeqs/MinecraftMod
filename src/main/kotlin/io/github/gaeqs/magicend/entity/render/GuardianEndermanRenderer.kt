package io.github.gaeqs.magicend.entity.render

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.entity.GuardianEnderman
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer
import net.minecraft.util.Identifier

class GuardianEndermanRenderer(context: EntityRenderDispatcher) :
    MobEntityRenderer<GuardianEnderman, GuardianEndermanModel<GuardianEnderman>>(context, GuardianEndermanModel(), 0.3f) {

    init {
        addFeature(object : EyesFeatureRenderer<GuardianEnderman, GuardianEndermanModel<GuardianEnderman>>(this) {
            private val SKIN =
                RenderLayer.getEyes(Identifier(MinecraftMod.MOD_ID, "textures/entity/guardian_enderman/eyes.png"))

            override fun getEyesTexture() = SKIN
        })
    }

    override fun getTexture(entity: GuardianEnderman): Identifier {
        return Identifier(MinecraftMod.MOD_ID, "textures/entity/guardian_enderman/main.png")
    }
}
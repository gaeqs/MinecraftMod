package io.github.gaeqs.magicend.entity.render

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.entity.ShamanEnderman
import io.github.gaeqs.magicend.entity.render.feature.TopTextureFeature
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier

class ShamanEndermanRenderer(context: EntityRenderDispatcher) :
    MobEntityRenderer<ShamanEnderman, ShamanEndermanModel<ShamanEnderman>>(context, ShamanEndermanModel(), 0.3f) {

    init {
        addFeature(TopTextureFeature(this, Math.PI.toFloat() / 20.0f, 0.2f))
    }

    override fun getTexture(entity: ShamanEnderman): Identifier {
        return Identifier(MinecraftMod.MOD_ID, "textures/entity/shaman_enderman/main.png")
    }
}
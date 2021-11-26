package io.github.gaeqs.magicend.entity.render

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.entity.VoidSquid
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier

class VoidSquidRenderer(context: EntityRenderDispatcher) :
    MobEntityRenderer<VoidSquid, VoidSquidModel<VoidSquid>>(context, VoidSquidModel(), 0.3f) {

    override fun getTexture(entity: VoidSquid): Identifier {
        return Identifier(MinecraftMod.MOD_ID, "textures/entity/void_squid/main.png")
    }
}
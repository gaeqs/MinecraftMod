package io.github.gaeqs.magicend.entity.render

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.entity.VoidWorm
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier

class VoidWormRenderer(context: EntityRenderDispatcher) :
    MobEntityRenderer<VoidWorm, VoidWormModel<VoidWorm>>(context, VoidWormModel(), 0.3f) {

    override fun getTexture(entity: VoidWorm): Identifier {
        return Identifier(MinecraftMod.MOD_ID, "textures/entity/void_worm/main.png")
    }
}
package io.github.gaeqs.magicend.entity.render

import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.entity.VoidSnake
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.util.Identifier

class VoidSnakeRenderer(context: EntityRenderDispatcher) :
    MobEntityRenderer<VoidSnake, VoidSnakeModel<VoidSnake>>(context, VoidSnakeModel(), 0.3f) {

    override fun getTexture(entity: VoidSnake): Identifier {
        return Identifier(MinecraftMod.MOD_ID, "textures/entity/void_snake/main.png")
    }
}
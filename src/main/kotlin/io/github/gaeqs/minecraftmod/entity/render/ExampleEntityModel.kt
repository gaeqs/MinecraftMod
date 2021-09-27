package io.github.gaeqs.minecraftmod.entity.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

class ExampleEntityModel<T : Entity>(extra : Float = 0.0f) : EntityModel<T>() {

    private val main: ModelPart
    private val head: ModelPart

    init {
        textureWidth = 64
        textureHeight = 64

        main = ModelPart(this);
        main.setPivot(0.0F, 24.0F, 0.0F);
        main.setTextureOffset(0, 0)
            .addCuboid(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, extra, false);

        head = ModelPart(this);
        head.setPivot(0.0F, 0.0F, 0.0F);
        main.addChild(head);
        head.setTextureOffset(0, 12)
            .addCuboid(-1.0F, -8.0F, -1.0F, 2.0F, 2.0F, 2.0F, extra, false);
    }

    override fun setAngles(
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {

        head.pitch = headPitch * 0.017453292f
        head.yaw = headYaw * 0.017453292f

    }

    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        main.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

    override fun animateModel(entity: T, limbAngle: Float, limbDistance: Float, tickDelta: Float) {
        super.animateModel(entity, limbAngle, limbDistance, tickDelta)
    }

}
package io.github.gaeqs.magicend.entity.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper

class GuardianEndermanModel<T : LivingEntity>(extra: Float = 0.0f) : EntityModel<T>() {
    private var body: ModelPart
    private var head: ModelPart
    private var helmet: ModelPart
    private var armR: ModelPart
    private var armL: ModelPart
    private var legR: ModelPart
    private var legL: ModelPart

    init {
        textureWidth = 128
        textureHeight = 128
        body = ModelPart(this)
        body.setPivot(0.0f, 30.0f, 1.0f)
        body.setTextureOffset(73, 14).addCuboid(-7.0f, -48.0f, -4.0f, 14.0f, 8.0f, 8.0f, 0.0f, false)
        body.setTextureOffset(76, 34).addCuboid(-5.0f, -40.0f, -2.0f, 10.0f, 8.0f, 4.0f, 0.0f, false)

        head = ModelPart(this)
        head.setPivot(0.0f, -48.0f, 0.0f)
        body.addChild(head)
        head.setTextureOffset(0, 0).addCuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.0f, false)

        helmet = ModelPart(this)
        helmet.setPivot(0.0f, 0.0f, 0.0f)
        head.addChild(helmet)
        helmet.setTextureOffset(74, 0).addCuboid(-5.0f, -9.0f, -5.0f, 10.0f, 3.0f, 10.0f, 0.0f, false)
        helmet.setTextureOffset(75, 50).addCuboid(-5.0f, -6.0f, 0.0f, 10.0f, 3.0f, 5.0f, 0.0f, false)
        helmet.setTextureOffset(76, 1).addCuboid(-1.0f, -6.0f, -5.0f, 2.0f, 4.0f, 1.0f, 0.0f, false)

        armR = ModelPart(this)
        armR.setPivot(-7.0f, -47.0f, 0.0f)
        body.addChild(armR)
        armR.setTextureOffset(33, 0).addCuboid(-5.0f, -2.0f, -3.0f, 5.0f, 11.0f, 5.0f, 0.0f, false)
        armR.setTextureOffset(12, 23).addCuboid(-3.0f, 9.0f, -2.0f, 3.0f, 16.0f, 3.0f, 0.0f, false)

        armL = ModelPart(this)
        armL.setPivot(7.0f, -47.0f, 0.0f)
        body.addChild(armL)
        armL.setTextureOffset(53, 0).addCuboid(0.0f, -2.0f, -3.0f, 5.0f, 11.0f, 5.0f, 0.0f, false)
        armL.setTextureOffset(0, 23).addCuboid(0.0f, 9.0f, -2.0f, 3.0f, 16.0f, 3.0f, 0.0f, false)

        legR = ModelPart(this)
        legR.setPivot(-2.0f, -32.0f, 0.0f)
        body.addChild(legR)
        legR.setTextureOffset(44, 23).addCuboid(-2.0f, 0.0f, -1.0f, 3.0f, 26.0f, 2.0f, 0.0f, false)

        legL = ModelPart(this)
        legL.setPivot(2.0f, -32.0f, 0.0f)
        body.addChild(legL)
        legL.setTextureOffset(26, 23).addCuboid(-1.0f, 0.0f, -1.0f, 3.0f, 26.0f, 2.0f, 0.0f, false)
    }

    override fun setAngles(
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        var k = 1.0f
        if (entity.roll > 4) {
            k = entity.velocity.lengthSquared().toFloat()
            k /= 0.2f
            k *= k * k
        }

        if (k < 1.0f) {
            k = 1.0f
        }

        head.pitch = headPitch * 0.017453292f
        head.yaw = headYaw * 0.017453292f

        armR.pitch = MathHelper.cos(limbAngle * 0.6662f + 3.1415927f) * 2.0f * limbDistance * 0.5f / k
        armL.pitch = MathHelper.cos(limbAngle * 0.6662f) * 2.0f * limbDistance * 0.5f / k
        armR.roll = 0.0f
        armL.roll = 0.0f

        legR.pitch = MathHelper.cos(limbAngle * 0.6662f)  * limbDistance / k
        legL.pitch = MathHelper.cos(limbAngle * 0.6662f + 3.1415927f) * limbDistance / k
        legR.yaw = 0.0f
        legL.yaw = 0.0f
        legR.roll = 0.0f
        legL.roll = 0.0f
    }

    override fun render(
        matrices: MatrixStack?,
        vertices: VertexConsumer?,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        body.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }
}
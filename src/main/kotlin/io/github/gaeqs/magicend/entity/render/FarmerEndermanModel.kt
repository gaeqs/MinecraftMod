package io.github.gaeqs.magicend.entity.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper

class FarmerEndermanModel<T : LivingEntity>(extra: Float = 0.0f) : EntityModel<T>() {
    private var body: ModelPart
    private var head: ModelPart
    private var hat: ModelPart
    private var forearmR: ModelPart
    private var armR: ModelPart
    private var forearmL: ModelPart
    private var armL: ModelPart
    private var legR: ModelPart
    private var legL: ModelPart

    init {
        textureWidth = 128
        textureHeight = 128

        body = ModelPart(this)
        body.setPivot(0.0f, 34.0f, -3.0f)
        body.setTextureOffset(0, 56).addCuboid(-4.0f, -48.0f, 2.0f, 8.0f, 12.0f, 4.0f, 0.0f, false)

        head = ModelPart(this)
        head.setPivot(0.0f, -48.0f, 4.0f)
        body.addChild(head)
        head.setTextureOffset(0, 0).addCuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, 0.0f, false)

        hat = ModelPart(this)
        hat.setPivot(0.0f, 49.0f, -4.0f)
        head.addChild(hat)
        hat.setTextureOffset(41, 4).addCuboid(-5.0f, -58.0f, 0.0f, 10.0f, 3.0f, 8.0f, 0.0f, false)
        hat.setTextureOffset(41, 18).addCuboid(-5.0f, -55.0f, 2.0f, 1.0f, 5.0f, 4.0f, 0.0f, false)
        hat.setTextureOffset(55, 19).addCuboid(4.0f, -55.0f, 2.0f, 1.0f, 5.0f, 4.0f, 0.0f, false)

        armR = ModelPart(this)
        armR.setPivot(-5.0f, -47.0f, 4.0f)
        body.addChild(armR)
        armR.setTextureOffset(9, 16).addCuboid(-1.0f, -1.0f, -1.0f, 2.0f, 12.0f, 2.0f, 0.0f, false)

        forearmR = ModelPart(this)
        forearmR.setPivot(0.0f, 10.5f, 0.0f)
        armR.addChild(forearmR)
        forearmR.setTextureOffset(9, 34).addCuboid(-1.0f, 0.5f, -1.0f, 2.0f, 15.0f, 2.0f, 0.0f, false)

        armL = ModelPart(this)
        armL.setPivot(5.0f, -47.0f, 4.0f)
        body.addChild(armL)
        armL.setTextureOffset(0, 16).addCuboid(-1.0f, -1.0f, -1.0f, 2.0f, 12.0f, 2.0f, 0.0f, false)

        forearmL = ModelPart(this)
        forearmL.setPivot(0.0f, 10.5f, 0.0f)
        armL.addChild(forearmL)
        forearmL.setTextureOffset(0, 34).addCuboid(-1.0f, 0.5f, -1.0f, 2.0f, 15.0f, 2.0f, 0.0f, false)

        legR = ModelPart(this)
        legR.setPivot(-2.25f, -36.25f, 4.0f)
        body.addChild(legR)
        legR.setTextureOffset(18, 27).addCuboid(-0.75f, 0.25f, -1.0f, 2.0f, 26.0f, 2.0f, 0.0f, false)
        legR.setTextureOffset(0, 74).addCuboid(-1.75f, 0.25f, -2.0f, 3.0f, 13.0f, 4.0f, 0.0f, false)

        legL = ModelPart(this)
        legL.setPivot(2.25f, -36.25f, 4.0f)
        body.addChild(legL)
        legL.setTextureOffset(27, 27).addCuboid(-1.25f, 0.25f, -1.0f, 2.0f, 26.0f, 2.0f, 0.0f, false)
        legL.setTextureOffset(0, 96).addCuboid(-1.25f, 0.25f, -2.0f, 3.0f, 13.0f, 4.0f, 0.0f, false)
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

        forearmR.pitch = armR.pitch
        forearmL.pitch = armL.pitch
        forearmR.roll = 0.0f
        forearmL.roll = 0.0f

        legR.pitch = MathHelper.cos(limbAngle * 0.6662f) * limbDistance / k
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
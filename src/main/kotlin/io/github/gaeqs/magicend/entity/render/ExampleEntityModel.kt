package io.github.gaeqs.magicend.entity.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper

class ExampleEntityModel<T : LivingEntity>(extra: Float = 0.0f) : EntityModel<T>() {

    private val body: ModelPart
    private val legL: ModelPart
    private val footL: ModelPart
    private val legR: ModelPart
    private val footR: ModelPart
    private val armL: ModelPart
    private val forearmL: ModelPart
    private val armR: ModelPart
    private val forearmR: ModelPart
    private val head: ModelPart

    init {
        textureWidth = 64
        textureHeight = 64
        body = ModelPart(this)
        body.setPivot(0.0f, 24.0f, 0.0f)
        body.setTextureOffset(26, 28).addCuboid(-3.0f, -6.0f, -2.0f, 5.0f, 3.0f, 3.0f, 0.0f, false)
        body.setTextureOffset(20, 20).addCuboid(-3.0f, -9.0f, -3.0f, 5.0f, 3.0f, 5.0f, 0.0f, false)

        legL = ModelPart(this)
        legL.setPivot(1.0f, -3.0f, 0.0f)
        body.addChild(legL)
        legL.setTextureOffset(12, 51).addCuboid(-1.0f, 0.0f, -2.0f, 2.0f, 2.0f, 3.0f, 0.0f, false)

        footL = ModelPart(this)
        footL.setPivot(-4.0f, 9.0f, 0.0f)
        legL.addChild(footL)
        footL.setTextureOffset(0, 51).addCuboid(3.0f, -7.0f, -3.0f, 2.0f, 1.0f, 4.0f, 0.0f, false)

        legR = ModelPart(this)
        legR.setPivot(-2.0f, -3.0f, 0.0f)
        body.addChild(legR)
        legR.setTextureOffset(12, 51).addCuboid(-1.0f, 0.0f, -2.0f, 2.0f, 2.0f, 3.0f, 0.0f, false)

        footR = ModelPart(this)
        footR.setPivot(-1.0f, 9.0f, 0.0f)
        legR.addChild(footR)
        footR.setTextureOffset(0, 51).addCuboid(0.0f, -7.0f, -3.0f, 2.0f, 1.0f, 4.0f, 0.0f, false)

        armL = ModelPart(this)
        armL.setPivot(2.0f, -8.0f, 0.0f)
        body.addChild(armL)
        armL.setTextureOffset(0, 14).addCuboid(0.0f, -1.0f, -2.0f, 2.0f, 3.0f, 3.0f, 0.0f, false)

        forearmL = ModelPart(this)
        forearmL.setPivot(-5.0f, 14.0f, 0.0f)
        armL.addChild(forearmL)
        forearmL.setTextureOffset(0, 20).addCuboid(5.0f, -12.0f, -2.0f, 1.0f, 2.0f, 3.0f, 0.0f, false)

        armR = ModelPart(this)
        armR.setPivot(-3.0f, -8.0f, 0.0f)
        body.addChild(armR)
        armR.setTextureOffset(0, 14).addCuboid(-2.0f, -1.0f, -2.0f, 2.0f, 3.0f, 3.0f, 0.0f, false)

        forearmR = ModelPart(this)
        forearmR.setPivot(-6.0f, 14.0f, 0.0f)
        armR.addChild(forearmR)
        forearmR.setTextureOffset(0, 20).addCuboid(5.0f, -12.0f, -2.0f, 1.0f, 2.0f, 3.0f, 0.0f, false)

        head = ModelPart(this)
        head.setPivot(0.0f, -9.0f, -1.0f)
        body.addChild(head)
        head.setTextureOffset(0, 39).addCuboid(-3.0f, -1.0f, -2.0f, 5.0f, 1.0f, 5.0f, 0.0f, false)
        head.setTextureOffset(0, 29).addCuboid(-4.0f, -5.0f, -2.0f, 7.0f, 4.0f, 6.0f, 0.0f, false)
        head.setTextureOffset(0, 45).addCuboid(-3.0f, -6.0f, -2.0f, 5.0f, 1.0f, 5.0f, 0.0f, false)
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

        legR.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance / k
        legL.pitch = MathHelper.cos(limbAngle * 0.6662f + 3.1415927f) * 1.4f * limbDistance / k
        legR.yaw = 0.0f
        legL.yaw = 0.0f
        legR.roll = 0.0f
        legL.roll = 0.0f

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
        body.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }

    override fun animateModel(entity: T, limbAngle: Float, limbDistance: Float, tickDelta: Float) {
        super.animateModel(entity, limbAngle, limbDistance, tickDelta)
    }

}
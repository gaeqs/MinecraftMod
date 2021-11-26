package io.github.gaeqs.magicend.entity.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.abs

class VoidSquidModel<T : LivingEntity>(extra: Float = 0.0f) : EntityModel<T>() {

    private var head: ModelPart
    private var body: ModelPart
    private var tentacle_1: ModelPart
    private var part_11: ModelPart
    private var part_12: ModelPart
    private var part_13: ModelPart
    private var tentacle_2: ModelPart
    private var part_21: ModelPart
    private var part_22: ModelPart
    private var part_23: ModelPart
    private var tentacle_3: ModelPart
    private var part_31: ModelPart
    private var part_32: ModelPart
    private var part_33: ModelPart
    private var tentacle_4: ModelPart
    private var part_41: ModelPart
    private var part_42: ModelPart
    private var part_43: ModelPart

    init {
        textureWidth = 256
        textureHeight = 256
        head = ModelPart(this)
        head.setPivot(0.0f, 2.0f, 0.0f)
        head.setTextureOffset(0, 0).addCuboid(-5.0f, -3.0f, -5.0f, 9.0f, 3.0f, 9.0f, 0.0f, false)
        head.setTextureOffset(0, 31).addCuboid(-4.0f, -6.0f, -3.0f, 7.0f, 3.0f, 6.0f, 0.0f, false)
        head.setTextureOffset(0, 67).addCuboid(-3.0f, -4.0f, -4.0f, 2.0f, 1.0f, 1.0f, 0.0f, false)
        head.setTextureOffset(0, 76).addCuboid(0.0f, -4.0f, -4.0f, 2.0f, 1.0f, 1.0f, 0.0f, false)
        head.setTextureOffset(0, 109).addCuboid(4.0f, -2.0f, -1.0f, 2.0f, 1.0f, 1.0f, 0.0f, false)
        head.setTextureOffset(3, 142).addCuboid(-7.0f, -2.0f, -1.0f, 2.0f, 1.0f, 1.0f, 0.0f, false)
        body = ModelPart(this)
        body.setPivot(0.0f, 2.0f, 0.0f)
        body.setTextureOffset(62, 100).addCuboid(-4.0f, 0.0f, -4.0f, 7.0f, 1.0f, 7.0f, 0.0f, false)
        tentacle_1 = ModelPart(this)
        tentacle_1.setPivot(-3.0f, 1.0f, -3.0f)
        body.addChild(tentacle_1)
        part_11 = ModelPart(this)
        part_11.setPivot(0.0f, 0.0f, 0.0f)
        tentacle_1.addChild(part_11)
        part_11.setTextureOffset(66, 5).addCuboid(-1.0f, 0.0f, -1.0f, 2.0f, 6.0f, 2.0f, 0.0f, false)
        part_12 = ModelPart(this)
        part_12.setPivot(-0.5f, 6.0f, 0.0f)
        part_11.addChild(part_12)
        part_12.setTextureOffset(67, 33).addCuboid(-0.5f, 0.0f, -1.0f, 1.0f, 8.0f, 2.0f, 0.0f, false)
        part_13 = ModelPart(this)
        part_13.setPivot(0.0f, 8.5f, 0.5f)
        part_12.addChild(part_13)
        part_13.setTextureOffset(67, 58).addCuboid(0.5f, -0.5f, -0.5f, 1.0f, 7.0f, 1.0f, 0.0f, false)
        tentacle_2 = ModelPart(this)
        tentacle_2.setPivot(2.0f, 1.0f, 0.0f)
        body.addChild(tentacle_2)
        part_21 = ModelPart(this)
        part_21.setPivot(0.0f, 0.0f, -3.0f)
        tentacle_2.addChild(part_21)
        part_21.setTextureOffset(91, 38).addCuboid(-1.0f, 0.0f, -1.0f, 2.0f, 6.0f, 2.0f, 0.0f, false)
        part_22 = ModelPart(this)
        part_22.setPivot(0.5f, 6.0f, 0.0f)
        part_21.addChild(part_22)
        part_22.setTextureOffset(92, 59).addCuboid(-0.5f, 0.0f, -1.0f, 1.0f, 8.0f, 2.0f, 0.0f, false)
        part_23 = ModelPart(this)
        part_23.setPivot(-1.0f, 7.5f, 0.5f)
        part_22.addChild(part_23)
        part_23.setTextureOffset(92, 75).addCuboid(-0.5f, 0.5f, -0.5f, 1.0f, 7.0f, 1.0f, 0.0f, false)
        tentacle_3 = ModelPart(this)
        tentacle_3.setPivot(-3.0f, 1.0f, 2.0f)
        body.addChild(tentacle_3)
        part_31 = ModelPart(this)
        part_31.setPivot(0.0f, 0.5f, 0.0f)
        tentacle_3.addChild(part_31)
        part_31.setTextureOffset(127, 7).addCuboid(-1.0f, -0.5f, -1.0f, 2.0f, 5.0f, 2.0f, 0.0f, false)
        part_32 = ModelPart(this)
        part_32.setPivot(-0.5f, 5.0f, 0.0f)
        part_31.addChild(part_32)
        part_32.setTextureOffset(126, 30).addCuboid(-0.5f, -0.5f, -1.0f, 1.0f, 7.0f, 2.0f, 0.0f, false)
        part_33 = ModelPart(this)
        part_33.setPivot(1.0f, 6.5f, -0.5f)
        part_32.addChild(part_33)
        part_33.setTextureOffset(131, 65).addCuboid(-0.5f, 0.0f, -0.5f, 1.0f, 6.0f, 1.0f, 0.0f, false)
        tentacle_4 = ModelPart(this)
        tentacle_4.setPivot(2.0f, 1.0f, 2.0f)
        body.addChild(tentacle_4)
        part_41 = ModelPart(this)
        part_41.setPivot(0.0f, 0.5f, 0.0f)
        tentacle_4.addChild(part_41)
        part_41.setTextureOffset(164, 38).addCuboid(-1.0f, -0.5f, -1.0f, 2.0f, 5.0f, 2.0f, 0.0f, false)
        part_42 = ModelPart(this)
        part_42.setPivot(0.5f, 5.0f, 0.0f)
        part_41.addChild(part_42)
        part_42.setTextureOffset(166, 47).addCuboid(-0.5f, -0.5f, -1.0f, 1.0f, 7.0f, 2.0f, 0.0f, false)
        part_43 = ModelPart(this)
        part_43.setPivot(-1.0f, 6.5f, -0.5f)
        part_42.addChild(part_43)
        part_43.setTextureOffset(166, 77).addCuboid(-0.5f, 0.0f, -0.5f, 1.0f, 6.0f, 1.0f, 0.0f, false)
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


      //  parts.forEachIndexed { i, it ->
      //      it.yaw =
      //          MathHelper.cos(animationProgress * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.01f * (1 + abs(i - 2))
      //  }
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
        head.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        body.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }
}
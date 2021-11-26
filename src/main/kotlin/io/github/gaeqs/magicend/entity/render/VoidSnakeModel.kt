package io.github.gaeqs.magicend.entity.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.abs

class VoidSnakeModel<T : LivingEntity>(extra: Float = 0.0f) : EntityModel<T>() {

    private var head: ModelPart
    private var maw_top: ModelPart
    private var maw_bottom: ModelPart
    private var tail: ModelPart

    private val parts: Array<ModelPart>

    init {
        textureWidth = 256
        textureHeight = 256
        parts = Array(5) { ModelPart(this) }
        head = ModelPart(this)
        head.setPivot(0.5f, 15.0f, -18.0f)
        head.setTextureOffset(0, 47).addCuboid(-3.5f, -4.0f, -1.0f, 7.0f, 8.0f, 2.0f, 0.0f, false)
        maw_top = ModelPart(this)
        maw_top.setPivot(-0.5f, -2.0f, -1.0f)
        head.addChild(maw_top)
        maw_top.setTextureOffset(0, 0).addCuboid(-3.0f, -2.0f, -5.0f, 7.0f, 3.0f, 5.0f, 0.0f, false)
        maw_top.setTextureOffset(0, 11).addCuboid(-2.0f, -4.0f, -3.0f, 2.0f, 2.0f, 2.0f, 0.0f, false)
        maw_top.setTextureOffset(10, 12).addCuboid(1.0f, -4.0f, -3.0f, 2.0f, 2.0f, 2.0f, 0.0f, false)
        maw_top.setTextureOffset(0, 17).addCuboid(-3.0f, 1.0f, -5.0f, 2.0f, 1.0f, 1.0f, 0.0f, false)
        maw_top.setTextureOffset(9, 20).addCuboid(2.0f, 1.0f, -5.0f, 2.0f, 1.0f, 1.0f, 0.0f, false)
        maw_bottom = ModelPart(this)
        maw_bottom.setPivot(0.0f, 3.25f, -1.5f)
        head.addChild(maw_bottom)
        maw_bottom.setTextureOffset(0, 36).addCuboid(-3.5f, -1.25f, -4.5f, 7.0f, 2.0f, 5.0f, 0.0f, false)
        maw_bottom.setTextureOffset(15, 34).addCuboid(-1.5f, -2.25f, -4.5f, 3.0f, 1.0f, 1.0f, 0.0f, false)
        tail = ModelPart(this)
        tail.setPivot(0.0f, 19.0f, -17.0f)

        parts[0].setPivot(0.5f, 0.75f, 0.0f)
        tail.addChild(parts[0])
        parts[0].setTextureOffset(0, 84).addCuboid(-4.5f, -4.75f, 0.0f, 9.0f, 9.0f, 5.0f, 0.0f, false)
        parts[0].setTextureOffset(48, 95).addCuboid(-2.5f, -6.75f, 0.0f, 5.0f, 2.0f, 3.0f, 0.0f, false)

        parts[1].setPivot(0.0f, -0.75f, 5.0f)
        parts[0].addChild(parts[1])
        parts[1].setTextureOffset(1, 134).addCuboid(-3.5f, -1.0f, 0.0f, 7.0f, 6.0f, 9.0f, 0.0f, false)
        parts[1].setTextureOffset(55, 162).addCuboid(-2.5f, -3.0f, 0.0f, 5.0f, 2.0f, 7.0f, 0.0f, false)

        parts[2].setPivot(0.0f, 2.25f, 9.0f)
        parts[1].addChild(parts[2])
        parts[2].setTextureOffset(8, 188).addCuboid(-2.5f, -0.25f, 0.0f, 5.0f, 3.0f, 10.0f, 0.0f, false)
        parts[2].setTextureOffset(61, 198).addCuboid(-1.5f, -2.25f, 0.0f, 3.0f, 2.0f, 10.0f, 0.0f, false)

        parts[3].setPivot(0.0f, -0.25f, 10.5f)
        parts[2].addChild(parts[3])
        parts[3].setTextureOffset(205, 5).addCuboid(-3.5f, -3.0f, -0.5f, 7.0f, 6.0f, 9.0f, 0.0f, false)
        parts[3].setTextureOffset(164, 8).addCuboid(-2.5f, -5.0f, 0.5f, 5.0f, 2.0f, 7.0f, 0.0f, false)

        parts[4].setPivot(0.0f, 0.25f, 8.5f)
        parts[3].addChild(parts[4])
        parts[4].setTextureOffset(214, 62).addCuboid(-2.5f, -0.25f, 0.0f, 5.0f, 3.0f, 7.0f, 0.0f, false)
        parts[4].setTextureOffset(173, 54).addCuboid(-1.5f, -2.25f, 0.0f, 3.0f, 2.0f, 5.0f, 0.0f, false)
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


        parts.forEachIndexed { i, it ->
            it.yaw =
                MathHelper.cos(animationProgress * 0.9f + i * 0.15f * 3.1415927f) * 3.1415927f * 0.01f * (1 + abs(i - 2))
        }
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
        tail.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }
}
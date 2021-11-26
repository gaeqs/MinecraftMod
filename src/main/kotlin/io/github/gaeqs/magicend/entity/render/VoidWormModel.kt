package io.github.gaeqs.magicend.entity.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.MathHelper
import kotlin.math.abs

class VoidWormModel<T : LivingEntity>(extra: Float = 0.0f) : EntityModel<T>() {

    private var head: ModelPart
    private var maw_top: ModelPart
    private var maw_bottom: ModelPart
    private var tail: ModelPart

    private val parts: Array<ModelPart>

    init {
        textureWidth = 256
        textureHeight = 256
        parts = Array(3) { ModelPart(this) }
        head = ModelPart(this)
        head.setPivot(0.0f, 22.0f, -3.0f)
        maw_top = ModelPart(this)
        maw_top.setPivot(-1.0f, -1.0f, 0.0f)
        head.addChild(maw_top)
        maw_top.setTextureOffset(0, 0).addCuboid(-2.0f, -1.0f, -4.0f, 5.0f, 2.0f, 4.0f, 0.0f, false)
        maw_top.setTextureOffset(2, 10).addCuboid(-2.0f, 1.0f, -4.0f, 1.0f, 1.0f, 1.0f, 0.0f, false)
        maw_top.setTextureOffset(13, 8).addCuboid(2.0f, 1.0f, -4.0f, 1.0f, 1.0f, 1.0f, 0.0f, false)
        maw_top.setTextureOffset(2, 17).addCuboid(-2.0f, -2.0f, -2.0f, 2.0f, 1.0f, 1.0f, 0.0f, false)
        maw_top.setTextureOffset(13, 17).addCuboid(1.0f, -2.0f, -2.0f, 2.0f, 1.0f, 1.0f, 0.0f, false)

        maw_bottom = ModelPart(this)
        maw_bottom.setPivot(0.0f, 1.0f, 0.0f)
        head.addChild(maw_bottom)
        maw_bottom.setTextureOffset(2, 29).addCuboid(-3.0f, -1.0f, -2.0f, 5.0f, 2.0f, 2.0f, 0.0f, false)

        tail = ModelPart(this)
        tail.setPivot(0.0f, 22.0f, -2.0f)


        parts[0] = ModelPart(this)
        parts[0].setPivot(0.0f, 0.0f, 0.0f)
        tail.addChild(parts[0])
        parts[0].setTextureOffset(2, 43).addCuboid(-4.0f, -3.0f, -1.0f, 7.0f, 5.0f, 5.0f, 0.0f, false)

        parts[1] = ModelPart(this)
        parts[1].setPivot(0.0f, 0.0f, 5.0f)
        parts[0].addChild(parts[1])
        parts[1].setTextureOffset(9, 56).addCuboid(-3.0f, -2.0f, -1.0f, 5.0f, 4.0f, 6.0f, 0.0f, false)

        parts[2] = ModelPart(this)
        parts[2].setPivot(0.0f, 1.0f, 6.0f)
        parts[1].addChild(parts[2])
        parts[2].setTextureOffset(23, 69).addCuboid(-2.0f, -2.0f, -1.0f, 3.0f, 3.0f, 4.0f, 0.0f, false)   }

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
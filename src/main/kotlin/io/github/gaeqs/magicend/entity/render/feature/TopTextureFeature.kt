package io.github.gaeqs.magicend.entity.render.feature

import io.github.gaeqs.magicend.entity.TopTextured
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vector4f

class TopTextureFeature<T, M : EntityModel<T>>(
    context: FeatureRendererContext<T, M>,
    private val waveSpeed: Float = 0.0f,
    private val waveAmplitude: Float = 0.0f
) : FeatureRenderer<T, M>(context) where T : Entity, T : TopTextured {

    companion object {
        private val VERTICES = arrayOf(-0.5f to 0.5f, -0.5f to -0.5f, 0.5f to -0.5f, 0.5f to 0.5f)
    }

    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        entity: T,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        val texture = entity.topTexture ?: return
        val offset = entity.displayOffset

        val nanos = entity.age + tickDelta
        val wave = MathHelper.sin(nanos * waveSpeed) * waveAmplitude

        matrices.push()
        matrices.translate(offset.x, offset.y + wave, offset.z)

        val consumer = vertexConsumers.getBuffer(RenderLayer.getEntityAlpha(texture, 0.5f))

        for ((x, y) in VERTICES) {
            val vec = Vector4f(0.0f, 0.0f, 0.0f, 1.0f).apply { transform(matrices.peek().model) }
            consumer.vertex(
                vec.x + x, vec.y + y, vec.z,
                1.0f, 1.0f, 1.0f, 1.0f,
                x + 0.5f, 1.0f - y - 0.5f,
                OverlayTexture.DEFAULT_UV, light,
                0.0f, 0.0f, -1.0f
            )

        }

        matrices.pop()
    }
}
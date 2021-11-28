package io.github.gaeqs.magicend.block.renderer

import io.github.gaeqs.magicend.block.entity.EnderBreadPlateBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Vec3f


class EnderBreadPlateBlockEntityRenderer(dispatcher: BlockEntityRenderDispatcher) :
    BlockEntityRenderer<EnderBreadPlateBlockEntity>(dispatcher) {

    private val stack = ItemStack(Items.COOKED_BEEF, 1)

    override fun render(
        entity: EnderBreadPlateBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {

        repeat(entity.amount) {
            matrices.push()

            val x = it % 3
            val y = it / 3

            matrices.translate(0.2 + x * 0.3, 0.7, 0.3 + y * 0.3)
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(10.0f))
            matrices.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(80.0f))

            val lightAbove = WorldRenderer.getLightmapCoordinates(entity.world, entity.pos.up())

            MinecraftClient.getInstance().itemRenderer.renderItem(
                stack,
                ModelTransformation.Mode.GROUND,
                lightAbove,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers
            )

            matrices.pop()
        }
    }
}
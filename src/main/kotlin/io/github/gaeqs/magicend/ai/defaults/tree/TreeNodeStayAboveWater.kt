package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.mob.MobEntity
import net.minecraft.tag.FluidTags

class StayAboveWater(activity: Activity, val chance: Float) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val entity = entity
        if (entity !is MobEntity) return InvocationResult.FAIL


        if (entity.isTouchingWater
            && entity.getFluidHeight(FluidTags.WATER) > entity.method_29241()
            || entity.isInLava
        ) {
            if (entity.random.nextFloat() < this.chance) {
                entity.jumpControl.setActive()
            }
            return InvocationResult.WAIT
        }

        return InvocationResult.SUCCESS
    }

    override fun stop() {
    }

    class Builder(var chance: Float) : TreeNodeBuilder<StayAboveWater> {
        override fun build(activity: Activity) = StayAboveWater(activity, chance)
    }
}

fun TreeNodeParentBuilder<*>.stayAboveWater(chance: Float = 0.8f) = addChild(StayAboveWater.Builder(chance))

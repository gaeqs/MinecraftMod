package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import io.github.gaeqs.magicend.util.isEntityTargeteable
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity

class TreeNodeLookAround(
    activity: Activity,
) : TreeNode(activity) {

    private var lookingEntity: LivingEntity? = null

    override fun start() {
        if (entity !is MobEntity) return
        lookingEntity = ai
            .getMemory(MemoryTypes.VISIBLE_NEARBY_LIVING_ENTITIES)
            .orEmpty()
            .minByOrNull { it.pos.squaredDistanceTo(entity.pos) }
    }

    override fun tick(): InvocationResult {
        val look = lookingEntity ?: return InvocationResult.FAIL
        if (look.isDead || !entity.isEntityTargeteable(look, ai)) return InvocationResult.SUCCESS
        val entity = entity as MobEntity
        entity.lookControl.lookAt(look.x, look.eyeY, look.z)
        return InvocationResult.WAIT
    }

    class Builder : TreeNodeBuilder<TreeNodeLookAround> {
        override fun build(activity: Activity) = TreeNodeLookAround(activity)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.lookAtNearestLivingEntity() = addChild(TreeNodeLookAround.Builder())
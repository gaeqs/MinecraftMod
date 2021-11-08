package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import io.github.gaeqs.magicend.util.isEntityTargeteable
import net.minecraft.entity.LivingEntity

class TreeNodeFindNearestLivingEntities(activity: Activity) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val box = entity.boundingBox.expand(16.0, 16.0, 16.0)
        val list = entity.world.getEntitiesByClass(LivingEntity::class.java, box) {
            it != entity && it.isAlive
        }

        ai.remember(MemoryTypes.NEARBY_LIVING_ENTITIES, list)
        ai.remember(MemoryTypes.VISIBLE_NEARBY_LIVING_ENTITIES, list.filter { entity.isEntityTargeteable(it, ai) })

        return InvocationResult.SUCCESS
    }

    override fun stop() {
    }


    class Builder : TreeNodeBuilder<TreeNodeFindNearestLivingEntities> {
        override fun build(activity: Activity) = TreeNodeFindNearestLivingEntities(activity)
    }
}

fun TreeNodeParentBuilder<*>.findNearestLivingEntities() = addChild(TreeNodeFindNearestLivingEntities.Builder())

package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.ItemEntity

class TreeNodeFindNearestItemEntities(activity: Activity, val radius: Double) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val box = entity.boundingBox.expand(radius, radius, radius)
        val list = entity.world.getEntitiesByClass(ItemEntity::class.java, box) {
            it != entity && it.isAlive
        }


        ai.remember(MemoryTypes.NEARBY_ITEM_ENTITIES, list)
        ai.remember(MemoryTypes.VISIBLE_NEARBY_ITEM_ENTITIES, list.filter { entity.canSee(it) })

        return InvocationResult.SUCCESS
    }

    override fun stop() {
    }


    class Builder(var radius: Double) : TreeNodeBuilder<TreeNodeFindNearestItemEntities> {
        override fun build(activity: Activity) = TreeNodeFindNearestItemEntities(activity, radius)
    }
}

fun TreeNodeParentBuilder<*>.findNearestItemEntities(radius: Double = 16.0) =
    addChild(TreeNodeFindNearestItemEntities.Builder(radius))

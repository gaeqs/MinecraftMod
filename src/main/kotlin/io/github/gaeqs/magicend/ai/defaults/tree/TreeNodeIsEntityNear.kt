package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.LivingEntity

class TreeNodeIsEntityNear(activity: Activity, val condition: (LivingEntity) -> Boolean) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val visible = ai.getMemory(MemoryTypes.VISIBLE_NEARBY_LIVING_ENTITIES)
        if (visible.isNullOrEmpty()) return InvocationResult.FAIL
        return if (visible.any { it.isAlive && condition(it) }) InvocationResult.SUCCESS else InvocationResult.FAIL
    }

    override fun stop() {
    }


    class Builder(var condition: (LivingEntity) -> Boolean) : TreeNodeBuilder<TreeNodeIsEntityNear> {
        override fun build(activity: Activity) = TreeNodeIsEntityNear(activity, condition)
    }
}

fun TreeNodeParentBuilder<*>.isEntityNear(condition: (LivingEntity) -> Boolean) =
    addChild(TreeNodeIsEntityNear.Builder(condition))

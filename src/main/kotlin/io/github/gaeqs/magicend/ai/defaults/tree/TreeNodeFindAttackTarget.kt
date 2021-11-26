package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.LivingEntity

class TreeNodeFindAttackTarget(activity: Activity, val condition: (LivingEntity) -> Boolean) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {

        val visible = ai.getMemory(MemoryTypes.VISIBLE_NEARBY_LIVING_ENTITIES)
        if (visible.isNullOrEmpty()) return InvocationResult.FAIL

        visible.forEach {
            if (it.isAlive && condition(it)) {
                ai.remember(MemoryTypes.ATTACK_TARGET, it)
                return InvocationResult.SUCCESS
            }
        }

        return InvocationResult.FAIL
    }

    override fun stop() {
    }


    class Builder(var condition: (LivingEntity) -> Boolean) : TreeNodeBuilder<TreeNodeFindAttackTarget> {
        override fun build(activity: Activity) = TreeNodeFindAttackTarget(activity, condition)
    }
}

fun TreeNodeParentBuilder<*>.findAttackTarget(condition: (LivingEntity) -> Boolean) =
    addChild(TreeNodeFindAttackTarget.Builder(condition))

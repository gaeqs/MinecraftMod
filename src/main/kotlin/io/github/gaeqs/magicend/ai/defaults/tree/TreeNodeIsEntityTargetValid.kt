package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.LivingEntity

class TreeNodeIsAttackTargetValid(activity: Activity, val memory: MemoryType<out LivingEntity>) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val memory = ai.getMemory(memory) ?: return InvocationResult.FAIL

        if (memory.isDead || memory.squaredDistanceTo(entity) > 32 * 32
            || memory.world.registryKey != entity.world.registryKey
        ) {
            return InvocationResult.FAIL
        }

        return InvocationResult.SUCCESS
    }

    override fun stop() {
    }


    class Builder(var memory: MemoryType<out LivingEntity>) : TreeNodeBuilder<TreeNodeIsAttackTargetValid> {
        override fun build(activity: Activity) = TreeNodeIsAttackTargetValid(activity, memory)
    }
}

fun TreeNodeParentBuilder<*>.isAttackTargetValid(
    memory: MemoryType<out LivingEntity>
) = addChild(TreeNodeIsAttackTargetValid.Builder(memory))

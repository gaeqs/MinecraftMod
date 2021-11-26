package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.Hand

class TreeNodeAttack(activity: Activity, val cooldown: Long) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        if (ai.getMemory(MemoryTypes.ATTACK_IN_COOLDOWN) == true) return InvocationResult.FAIL

        val entity = entity
        if (entity !is MobEntity) return InvocationResult.FAIL
        val target = ai.getMemory(MemoryTypes.ATTACK_TARGET) ?: return InvocationResult.FAIL
        if (target.isDead) return InvocationResult.FAIL

        entity.swingHand(Hand.MAIN_HAND)
        if (!entity.tryAttack(target)) return InvocationResult.FAIL

        ai.remember(MemoryTypes.ATTACK_IN_COOLDOWN, true, cooldown)

        return InvocationResult.SUCCESS
    }

    override fun stop() {
    }

    class Builder(var cooldown: Long) : TreeNodeBuilder<TreeNodeAttack> {
        override fun build(activity: Activity) = TreeNodeAttack(activity, cooldown)
    }
}

fun TreeNodeParentBuilder<*>.attack(cooldown: Long = 20) = addChild(TreeNodeAttack.Builder(cooldown))

package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import io.github.gaeqs.magicend.entity.EnderVillager

class TreeNodeFindEnderVillager(
    activity: Activity,
    val saveOn: MemoryType<in EnderVillager>,
    val condition: (EnderVillager) -> Boolean
) :
    TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val entity = entity as? EnderVillager ?: return InvocationResult.FAIL

        val result = entity.village.villagers.find { it.isAlive && it != entity && condition(it) }
        if (result != null) {
            ai.remember(saveOn, result)
            return InvocationResult.SUCCESS
        }
        return InvocationResult.FAIL
    }

    override fun stop() {
    }

    class Builder(
        var saveOn: MemoryType<in EnderVillager>,
        var condition: (EnderVillager) -> Boolean
    ) : TreeNodeBuilder<TreeNodeFindEnderVillager> {
        override fun build(activity: Activity) = TreeNodeFindEnderVillager(activity, saveOn, condition)
    }
}

fun TreeNodeParentBuilder<*>.findEnderVillager(
    saveOn: MemoryType<in EnderVillager>,
    condition: (EnderVillager) -> Boolean
) = addChild(TreeNodeFindEnderVillager.Builder(saveOn, condition))

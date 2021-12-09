package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import io.github.gaeqs.magicend.entity.FarmerEnderman
import net.minecraft.util.dynamic.GlobalPos

class TreeNodeSetOccupied(
    activity: Activity,
    val memory: MemoryType<GlobalPos>,
    val occupy: Boolean = false
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val farmer = entity as? FarmerEnderman ?: return InvocationResult.FAIL
        val pos = ai.getMemory(memory) ?: return InvocationResult.FAIL
        if (occupy) {
            farmer.village.occupyFarmland(farmer, pos)
        } else {
            farmer.village.releaseFarmland(pos)
        }

        return InvocationResult.SUCCESS
    }

    class Builder(
        var memory: MemoryType<GlobalPos>,
        var occupy: Boolean
    ) : TreeNodeBuilder<TreeNodeSetOccupied> {
        override fun build(activity: Activity) = TreeNodeSetOccupied(activity, memory, occupy)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.setOccupied(
    memory: MemoryType<GlobalPos>,
    occupy: Boolean
) = addChild(TreeNodeSetOccupied.Builder(memory, occupy))
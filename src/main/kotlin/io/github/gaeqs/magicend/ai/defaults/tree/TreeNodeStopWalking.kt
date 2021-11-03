package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.ai.TargetFinder
import net.minecraft.entity.ai.brain.WalkTarget
import net.minecraft.entity.ai.pathing.Path
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class TreeNodeStopWalking(activity: Activity) : TreeNode(activity) {

    override fun reset() {
    }

    override fun invoke(): InvocationResult {
        val entity = activity.ai.entity
        if (entity !is PathAwareEntity) return InvocationResult.FAIL
        entity.navigation.stop()
        return InvocationResult.SUCCESS
    }

    class Builder : TreeNodeBuilder<TreeNodeStopWalking> {
        override fun build(activity: Activity) = TreeNodeStopWalking(activity)
    }
}

fun TreeNodeParentBuilder<*>.stopWalking() {
    children.add(TreeNodeStopWalking.Builder())
}

fun TreeNodeUniqueParentBuilder<*>.stopWalking() {
    child = TreeNodeStopWalking.Builder()
}
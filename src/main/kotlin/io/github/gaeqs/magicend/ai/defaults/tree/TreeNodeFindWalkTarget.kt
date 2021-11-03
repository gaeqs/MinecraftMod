package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.ai.TargetFinder
import net.minecraft.entity.ai.brain.WalkTarget
import net.minecraft.entity.mob.PathAwareEntity

class TreeNodeFindWalkTarget(
    activity: Activity,
    val speed: Float,
    val horizontalRadius: Int,
    val verticalRadius: Int
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val entity = activity.ai.entity
        if (entity !is PathAwareEntity) return InvocationResult.FAIL
        val target = TargetFinder.findGroundTarget(entity, horizontalRadius, verticalRadius)
        if (target != null) {
            ai.remember(MemoryTypes.WALK_TARGET, WalkTarget(target, speed, 0))
            return InvocationResult.SUCCESS
        }

        ai.forget(MemoryTypes.WALK_TARGET)
        return InvocationResult.FAIL
    }

    class Builder(
        val speed: Float,
        val horizontalRadius: Int,
        val verticalRadius: Int
    ) : TreeNodeBuilder<TreeNodeFindWalkTarget> {
        override fun build(activity: Activity) =
            TreeNodeFindWalkTarget(activity, speed, horizontalRadius, verticalRadius)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.findWalkTarget(speed: Float, horizontalRadius: Int = 10, verticalRadius: Int = 7) =
    addChild(TreeNodeFindWalkTarget.Builder(speed, horizontalRadius, verticalRadius))
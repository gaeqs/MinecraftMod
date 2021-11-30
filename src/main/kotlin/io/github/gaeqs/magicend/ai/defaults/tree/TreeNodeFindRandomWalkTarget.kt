package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.ai.TargetFinder
import net.minecraft.entity.ai.brain.WalkTarget
import net.minecraft.entity.mob.PathAwareEntity

class TreeNodeFindRandomWalkTarget(
    activity: Activity,
    val speed: Float,
    val horizontalRadius: Int,
    val verticalRadius: Int,
    val air: Boolean
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val entity = activity.ai.entity
        if (entity !is PathAwareEntity) return InvocationResult.FAIL

        val target = if (!air)
            TargetFinder.findGroundTarget(entity, horizontalRadius, verticalRadius)
        else {
            val vec = entity.pos.add(
                entity.random.nextDouble() * 10 - 5,
                entity.random.nextDouble() * 10 - 5,
                entity.random.nextDouble() * 10 - 5
            ).subtract(entity.pos).normalize()
            TargetFinder.findAirTarget(
                entity, horizontalRadius, verticalRadius, vec,
                1.5707964f, 2, 1
            )
        }
        if (target != null) {
            ai.remember(MemoryTypes.WALK_TARGET, WalkTarget(target, speed, 0))
            return InvocationResult.SUCCESS
        }

        ai.forget(MemoryTypes.WALK_TARGET)
        return InvocationResult.FAIL
    }

    class Builder(
        var speed: Float,
        var horizontalRadius: Int,
        var verticalRadius: Int,
        var air: Boolean
    ) : TreeNodeBuilder<TreeNodeFindRandomWalkTarget> {
        override fun build(activity: Activity) =
            TreeNodeFindRandomWalkTarget(activity, speed, horizontalRadius, verticalRadius, air)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.findRandomWalkTarget(
    speed: Float,
    horizontalRadius: Int = 10,
    verticalRadius: Int = 7,
    air: Boolean = false
) =
    addChild(TreeNodeFindRandomWalkTarget.Builder(speed, horizontalRadius, verticalRadius, air))
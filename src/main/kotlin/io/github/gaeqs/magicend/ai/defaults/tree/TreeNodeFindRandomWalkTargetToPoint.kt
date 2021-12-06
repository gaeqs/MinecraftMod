package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.canReachBlock
import io.github.gaeqs.magicend.ai.defaults.findPointOfInterest
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.ai.TargetFinder
import net.minecraft.entity.ai.brain.WalkTarget
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.poi.PointOfInterestType

class TreeNodeFindRandomWalkTargetToPoint(
    activity: Activity,
    val speed: Float,
    val point: PointOfInterestType,
    val area: Double,
    val horizontalRadius: Int,
    val verticalRadius: Int
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val entity = activity.ai.entity
        if (entity !is PathAwareEntity) return InvocationResult.FAIL
        val world = entity.world as ServerWorld


        val position = world.findPointOfInterest(entity.blockPos, point, 64) {
            entity.canReachBlock(it, point.searchDistance)
        }

        val target = if (position == null) {
            TargetFinder.findGroundTarget(entity, horizontalRadius, verticalRadius)
        } else {
            TargetFinder.findGroundTarget(entity, horizontalRadius, verticalRadius) {
                val randomWeight = entity.random.nextDouble() * area
                -it.getSquaredDistance(position) + randomWeight * randomWeight
            }
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
        var point: PointOfInterestType,
        var area: Double,
        var horizontalRadius: Int,
        var verticalRadius: Int,
    ) : TreeNodeBuilder<TreeNodeFindRandomWalkTargetToPoint> {
        override fun build(activity: Activity) =
            TreeNodeFindRandomWalkTargetToPoint(activity, speed, point, area, horizontalRadius, verticalRadius)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.findRandomWalkTargetToPoint(
    speed: Float,
    point: PointOfInterestType,
    area: Double = 20.0,
    horizontalRadius: Int = 10,
    verticalRadius: Int = 7
) =
    addChild(TreeNodeFindRandomWalkTargetToPoint.Builder(speed, point, area, horizontalRadius, verticalRadius))
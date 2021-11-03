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

class TreeNodeWalkToTarget(activity: Activity) : TreeNode(activity) {

    private var status = Status.INITIALIZING

    private var path: Path? = null
    private var lookTargetPos: BlockPos? = null

    override fun reset() {
    }

    override fun invoke(): InvocationResult {
        return when (status) {
            Status.INITIALIZING -> init()
            Status.RUNNING -> run()
            Status.FINISHED -> InvocationResult.SUCCESS
        }
    }

    private fun init(): InvocationResult {
        val entity = activity.ai.entity
        if (entity !is PathAwareEntity) return InvocationResult.FAIL

        val walkTarget = activity.ai.getMemory(MemoryTypes.WALK_TARGET)
        if (walkTarget == null || hasReached(walkTarget)) {
            status = Status.FINISHED
            return InvocationResult.FAIL
        }

        if (!hasFinishedPath(walkTarget)) {
            status = Status.FINISHED
            return InvocationResult.FAIL
        }

        status = Status.RUNNING
        return InvocationResult.WAIT
    }

    private fun run(): InvocationResult {
        val entity = activity.ai.entity as PathAwareEntity
        path = entity.navigation.currentPath
        val walkTarget = activity.ai.getMemory(MemoryTypes.WALK_TARGET)
        if (!shouldKeepRunning(walkTarget)) {
            status = Status.FINISHED
            return InvocationResult.SUCCESS
        }
        walkTarget!!

        if (walkTarget.lookTarget.blockPos.getSquaredDistance(lookTargetPos) > 4.0 && hasFinishedPath(walkTarget)) {
            lookTargetPos = walkTarget.lookTarget.blockPos
            entity.navigation.startMovingAlong(path, walkTarget.speed.toDouble())
        }
        return InvocationResult.WAIT
    }

    private fun shouldKeepRunning(walkTarget: WalkTarget?): Boolean {
        val entity = activity.ai.entity as PathAwareEntity
        if (path == null || lookTargetPos == null) return false
        return !entity.navigation.isIdle && walkTarget != null && !hasReached(walkTarget)
    }


    private fun hasReached(target: WalkTarget) =
        target.lookTarget.blockPos.getManhattanDistance(activity.ai.entity.blockPos) <= target.completionRange

    private fun hasFinishedPath(walkTarget: WalkTarget): Boolean {
        val entity = activity.ai.entity as PathAwareEntity
        val blockPos = walkTarget.lookTarget.blockPos
        var path = entity.navigation.findPathTo(blockPos, 0)

        if (path == null) {
            val vec = TargetFinder.findTargetTowards(
                entity, 10,
                7, Vec3d.ofBottomCenter(blockPos)
            )
            if (vec != null) {
                path = entity.navigation.findPathTo(vec.x, vec.y, vec.z, 0)
            }
        }

        if (path == null) return false

        this.path = path
        this.lookTargetPos = walkTarget.lookTarget.blockPos
        return true
    }

    private enum class Status {
        INITIALIZING,
        RUNNING,
        FINISHED
    }

    class Builder : TreeNodeBuilder<TreeNodeWalkToTarget> {
        override fun build(activity: Activity) = TreeNodeWalkToTarget(activity)
    }
}

fun TreeNodeParentBuilder<*>.walkToTarget() {
    children.add(TreeNodeWalkToTarget.Builder())
}

fun TreeNodeUniqueParentBuilder<*>.walkToTarget() {
    child = TreeNodeWalkToTarget.Builder()
}
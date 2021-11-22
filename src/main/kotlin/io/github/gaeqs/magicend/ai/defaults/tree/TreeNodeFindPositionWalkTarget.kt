package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.ai.TargetFinder
import net.minecraft.entity.ai.brain.WalkTarget
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.dynamic.GlobalPos
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class TreeNodeFindPositionWalkTarget(
    activity: Activity,
    val memory: MemoryType<GlobalPos>,
    val speed: Float,
    val horizontalRadius: Int,
    val verticalRadius: Int
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val entity = activity.ai.entity
        if (entity !is PathAwareEntity) return fail()
        val world = entity.world
        if (world !is ServerWorld) return fail()
        val position = ai.getMemory(memory) ?: return fail()
        if (position.dimension != world.registryKey) return fail()

        ai.remember(MemoryTypes.WALK_TARGET, WalkTarget(position.pos, speed, 0))

        var target: Vec3d? = null
        var i = 0;
        while (i < 1000 && target == null) {
            val candidate = TargetFinder.findTargetTowards(
                entity, horizontalRadius, verticalRadius,
                Vec3d.ofBottomCenter(position.pos)
            )
            if (candidate != null && !exceedsMaxRange(BlockPos(candidate))) {
                target = candidate
            }
            i++
        }

        if (target == null) fail()

        if (target != null) {
            ai.remember(MemoryTypes.WALK_TARGET, WalkTarget(position.pos, speed, 0))
            return InvocationResult.SUCCESS
        }

        return fail()
    }

    private fun fail(): InvocationResult {
        ai.forget(MemoryTypes.WALK_TARGET)
        return InvocationResult.FAIL
    }

    private fun exceedsMaxRange(pos: BlockPos): Boolean {
        return pos.getManhattanDistance(entity.blockPos) > 48;
    }

    class Builder(
        var memory: MemoryType<GlobalPos>,
        var speed: Float,
        var horizontalRadius: Int,
        var verticalRadius: Int
    ) : TreeNodeBuilder<TreeNodeFindPositionWalkTarget> {
        override fun build(activity: Activity) =
            TreeNodeFindPositionWalkTarget(activity, memory, speed, horizontalRadius, verticalRadius)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.findPositionWalkTarget(
    memory: MemoryType<GlobalPos>,
    speed: Float,
    horizontalRadius: Int = 15,
    verticalRadius: Int = 7
) =
    addChild(TreeNodeFindPositionWalkTarget.Builder(memory, speed, horizontalRadius, verticalRadius))
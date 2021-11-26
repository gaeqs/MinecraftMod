package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.Entity
import net.minecraft.entity.ai.brain.EntityLookTarget
import net.minecraft.entity.ai.brain.WalkTarget
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class TreeNodeFindEntityWalkTarget(
    activity: Activity,
    val memory: MemoryType<out Entity>,
    val speed: Float,
    val stopRange: Int
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val entity = activity.ai.entity
        if (entity !is PathAwareEntity) return fail()
        val world = entity.world
        if (world !is ServerWorld) return fail()
        val target = ai.getMemory(memory) ?: return fail()

        ai.remember(
            MemoryTypes.WALK_TARGET,
            WalkTarget(EntityLookTarget(target, false), speed, stopRange)
        )

        return InvocationResult.SUCCESS
    }

    private fun fail(): InvocationResult {
        ai.forget(MemoryTypes.WALK_TARGET)
        return InvocationResult.FAIL
    }

    private fun exceedsMaxRange(pos: BlockPos): Boolean {
        return pos.getManhattanDistance(entity.blockPos) > 48;
    }

    class Builder(
        var memory: MemoryType<out Entity>,
        var speed: Float,
        var stopRange: Int
    ) : TreeNodeBuilder<TreeNodeFindEntityWalkTarget> {
        override fun build(activity: Activity) =
            TreeNodeFindEntityWalkTarget(activity, memory, speed, stopRange)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.findEntityWalkTarget(
    memory: MemoryType<out Entity>,
    speed: Float,
    stopRange: Int
) =
    addChild(TreeNodeFindEntityWalkTarget.Builder(memory, speed, stopRange))
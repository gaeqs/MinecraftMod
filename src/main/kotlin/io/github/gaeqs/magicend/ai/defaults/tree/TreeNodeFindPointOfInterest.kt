package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.dynamic.GlobalPos
import net.minecraft.util.math.BlockPos
import net.minecraft.world.poi.PointOfInterestStorage
import net.minecraft.world.poi.PointOfInterestType

class TreeNodeFindPointOfInterest(
    activity: Activity,
    val point: PointOfInterestType,
    val memory: MemoryType<GlobalPos>,
    val radius: Int
) : TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val world = entity.world
        if (world !is ServerWorld) return InvocationResult.FAIL
        if (entity !is MobEntity) return InvocationResult.FAIL

        val position = world.pointOfInterestStorage.getPositions(
            point.completionCondition,
            { testBlockPos(it) },
            entity.blockPos,
            radius,
            PointOfInterestStorage.OccupationStatus.ANY
        ).findAny().orElse(null)

        if (position == null) {
            ai.forget(memory)
            return InvocationResult.FAIL
        }

        val globalPos = GlobalPos.create(world.registryKey, position)
        ai.remember(memory, globalPos)
        return InvocationResult.SUCCESS
    }

    private fun testBlockPos(pos: BlockPos): Boolean {
        val entity = entity as MobEntity
        val path = entity.navigation.findPathTo(pos, point.searchDistance)
        return path != null && path.reachesTarget()
    }

    class Builder(
        var point: PointOfInterestType,
        var memory: MemoryType<GlobalPos>,
        var radius: Int
    ) : TreeNodeBuilder<TreeNodeFindPointOfInterest> {
        override fun build(activity: Activity) = TreeNodeFindPointOfInterest(activity, point, memory, radius)
    }

    override fun stop() {

    }
}

fun TreeNodeParentBuilder<*>.findPointOfInterest(
    point: PointOfInterestType,
    memory: MemoryType<GlobalPos>,
    radius: Int
) = addChild(TreeNodeFindPointOfInterest.Builder(point, memory, radius))
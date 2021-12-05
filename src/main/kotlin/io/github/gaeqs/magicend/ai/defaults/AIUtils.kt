package io.github.gaeqs.magicend.ai.defaults

import net.minecraft.entity.Entity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.poi.PointOfInterestStorage
import net.minecraft.world.poi.PointOfInterestType

fun ServerWorld.findPointOfInterest(
    from: BlockPos,
    point: PointOfInterestType,
    radius: Int,
    condition: (BlockPos) -> Boolean
): BlockPos? {
    return pointOfInterestStorage.getPositions(
        point.completionCondition,
        condition,
        from,
        radius,
        PointOfInterestStorage.OccupationStatus.ANY
    ).findAny().orElse(null)

}

fun MobEntity.canReachBlock(pos: BlockPos, searchDistance: Int): Boolean {
    val path = navigation.findPathTo(pos, searchDistance)
    return path != null && path.reachesTarget()
}

fun PathAwareEntity.canNavigateToEntity(entity: Entity): Boolean {
    val path = navigation.findPathTo(entity, 0) ?: return false
    val pathNode = path.end ?: return false
    val i = pathNode.x - MathHelper.floor(entity.x)
    val j = pathNode.z - MathHelper.floor(entity.z)
    return (i * i + j * j).toDouble() <= 2.25
}

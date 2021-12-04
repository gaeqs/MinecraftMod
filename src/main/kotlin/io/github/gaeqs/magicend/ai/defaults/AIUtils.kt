package io.github.gaeqs.magicend.ai.defaults

import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
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
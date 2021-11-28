package io.github.gaeqs.magicend.ai.defaults

import com.google.common.collect.ImmutableSet
import io.github.gaeqs.magicend.MinecraftMod
import io.github.gaeqs.magicend.block.DragonStatueBlock
import io.github.gaeqs.magicend.block.EnderBreadPlateBlock
import net.fabricmc.fabric.mixin.`object`.builder.PointOfInterestTypeAccessor
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.poi.PointOfInterestType

object PointOfInterestTypes {

    val DRAGON_STATUE = register(
        Identifier(MinecraftMod.MOD_ID, "dragon_statue"),
        getAllStatesOf(DragonStatueBlock.BLOCK), 32, 100
    )

    val ENDER_BREAD_PLATE = register(
        Identifier(MinecraftMod.MOD_ID, "ender_bread_plate"),
        getAllStatesOf(EnderBreadPlateBlock.BLOCK), 32, 100
    )

    fun register(
        id: Identifier,
        blockStates: Set<BlockState>,
        ticketCount: Int,
        searchDistance: Int,
        completionCondition: ((PointOfInterestType) -> Boolean)? = null
    ): PointOfInterestType {
        val type = if (completionCondition != null)
            PointOfInterestTypeAccessor.callCreate(
                id.toString(),
                blockStates,
                ticketCount,
                completionCondition,
                searchDistance
            )
        else PointOfInterestTypeAccessor.callCreate(id.toString(), blockStates, ticketCount, searchDistance)

        Registry.register(Registry.POINT_OF_INTEREST_TYPE, id, type)
        PointOfInterestTypeAccessor.callSetup(type)
        return type
    }


    fun getAllStatesOf(block: Block): Set<BlockState> {
        return ImmutableSet.copyOf(block.stateManager.states)
    }
}
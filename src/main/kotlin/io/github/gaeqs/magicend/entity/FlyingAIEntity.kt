package io.github.gaeqs.magicend.entity

import io.github.gaeqs.magicend.ai.EntityAI
import io.github.gaeqs.magicend.ai.defaults.tree.stayAboveWater
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.and
import io.github.gaeqs.magicend.ai.tree.node.loopUnconditional
import io.github.gaeqs.magicend.ai.tree.node.rootSimultaneously
import io.github.gaeqs.magicend.ai.tree.node.wait
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.FlyingEntity
import net.minecraft.world.World

open class FlyingAIEntity(
    type: EntityType<out FlyingEntity>,
    world: World
) : FlyingEntity(type, world) {

    val ai = EntityAI(this)

    init {
        initAI()
    }

    override fun mobTick() {
        world.profiler.push("enderVillagerBrain")
        ai.tick()
        world.profiler.pop()
    }

    private fun initAI() {
        ai.coreActivity = TreeActivity("core", ai, rootSimultaneously {
            loopUnconditional {
                and {
                    stayAboveWater(0.5f)
                    wait(10)
                }
            }
        })
    }

}
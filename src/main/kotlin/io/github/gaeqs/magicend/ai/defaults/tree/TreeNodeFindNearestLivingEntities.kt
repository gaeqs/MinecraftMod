package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.TargetPredicate

class TreeNodeFindNearestLivingEntities(activity: Activity) : TreeNode(activity) {

    companion object {
        private val TARGET_CONDITIONS =
            TargetPredicate().setBaseMaxDistance(16.0).includeTeammates().ignoreEntityTargetRules()
        private val TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING =
            TargetPredicate().setBaseMaxDistance(16.0).includeTeammates()
                .ignoreEntityTargetRules().ignoreDistanceScalingFactor()
    }

    override fun start() {
    }

    override fun tick(): InvocationResult {
        val ai = activity.ai
        val entity = ai.entity
        val box = entity.boundingBox.expand(16.0, 16.0, 16.0)
        val list = entity.world.getEntitiesByClass(LivingEntity::class.java, box) {
            it != entity && it.isAlive
        }

        ai.remember(MemoryTypes.NEARBY_LIVING_ENTITIES, list)
        ai.remember(MemoryTypes.VISIBLE_NEARBY_LIVING_ENTITIES, list.filter { isEntityTargeteable(it) })

        return InvocationResult.SUCCESS
    }

    override fun stop() {
    }

    private fun isEntityTargeteable(target: LivingEntity): Boolean {
        val ai = activity.ai
        return if (ai.hasMemory(MemoryTypes.ATTACK_TARGET)) {
            TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(ai.entity, target)
        } else {
            TARGET_CONDITIONS.test(ai.entity, target)
        }
    }


    class Builder : TreeNodeBuilder<TreeNodeFindNearestLivingEntities> {
        override fun build(activity: Activity) = TreeNodeFindNearestLivingEntities(activity)
    }
}

fun TreeNodeParentBuilder<*>.findNearestLivingEntities() = addChild(TreeNodeFindNearestLivingEntities.Builder())

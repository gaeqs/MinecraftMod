package io.github.gaeqs.magicend.ai.defaults.tree

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.defaults.memory.MemoryTypes
import io.github.gaeqs.magicend.ai.memory.MemoryType
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity

class TreeNodeFindEntity<T : Entity>(
    activity: Activity,
    val list: MemoryType<out Collection<T>>,
    val saveOn: MemoryType<in T>,
    val condition: (T) -> Boolean
) :
    TreeNode(activity) {

    override fun start() {
    }

    override fun tick(): InvocationResult {

        val memory = ai.getMemory(list)
        if (memory.isNullOrEmpty()) return InvocationResult.FAIL

        memory.forEach {
            if (it.isAlive && condition(it)) {
                ai.remember(saveOn, it)
                return InvocationResult.SUCCESS
            }
        }

        return InvocationResult.FAIL
    }

    override fun stop() {
    }

    class Builder<T : Entity>(
        var list: MemoryType<out Collection<T>>,
        var saveOn: MemoryType<in T>,
        var condition: (T) -> Boolean
    ) : TreeNodeBuilder<TreeNodeFindEntity<T>> {
        override fun build(activity: Activity) = TreeNodeFindEntity(activity, list, saveOn, condition)
    }
}

fun <T : Entity> TreeNodeParentBuilder<*>.findEntity(
    list: MemoryType<out Collection<T>>,
    saveOn: MemoryType<in T>,
    condition: (T) -> Boolean
) = addChild(TreeNodeFindEntity.Builder(list, saveOn, condition))

fun TreeNodeParentBuilder<*>.findEntity(condition: (LivingEntity) -> Boolean) {
    findEntity(MemoryTypes.VISIBLE_NEARBY_LIVING_ENTITIES, MemoryTypes.ATTACK_TARGET, condition)
}

package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeLoopN(activity: Activity, val child: TreeNode, val times: Int) : TreeNode(activity) {

    private var executed = 0

    override fun tick(): InvocationResult {
        while (executed < times) {
            if (child.tick() == InvocationResult.WAIT) return InvocationResult.WAIT
            executed++
            if (executed < times) {
                child.stop()
                child.start()
            }
        }
        return InvocationResult.SUCCESS
    }

    override fun start() {
        child.start()
    }

    override fun stop() {
        child.stop()
    }

    class Builder(var times: Int) : TreeNodeUniqueParentBuilder<TreeNodeLoopN>() {
        override fun build(activity: Activity) = TreeNodeLoopN(activity, child.build(activity), times)
    }
}

inline fun TreeNodeParentBuilder<*>.loopN(times: Int, builder: TreeNodeLoopN.Builder.() -> Unit) =
    TreeNodeLoopN.Builder(times).also {
        addChild(it)
        builder(it)
    }

inline fun loopN(times: Int, builder: TreeNodeLoopN.Builder.() -> Unit): TreeNodeLoopN.Builder {
    return TreeNodeLoopN.Builder(times).also(builder)
}
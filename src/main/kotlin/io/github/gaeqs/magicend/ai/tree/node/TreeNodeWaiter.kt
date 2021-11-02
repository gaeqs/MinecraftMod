package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeParentBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeUniqueParentBuilder

class TreeNodeWaiter(val ticks: Int, val result: InvocationResult = InvocationResult.SUCCESS) : TreeNode() {

    private var current = 0

    override fun reset() {
        current = 0
    }

    override fun invoke(): InvocationResult {
        if (current < ticks) {
            current++
            return InvocationResult.WAIT
        }
        return result
    }

    class Builder(var ticks: Int) : TreeNodeBuilder<TreeNodeWaiter> {
        override fun build() = TreeNodeWaiter(ticks)
    }
}

 fun TreeNodeParentBuilder<*>.wait(times: Int) {
    val b = TreeNodeWaiter.Builder(times)
    children.add(b)
}

 fun TreeNodeUniqueParentBuilder<*>.wait(times: Int) {
    val b = TreeNodeWaiter.Builder(times)
    child = b
}
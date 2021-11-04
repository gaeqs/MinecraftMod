package io.github.gaeqs.magicend.ai.statemachine.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.statemachine.StateMachine
import io.github.gaeqs.magicend.ai.statemachine.builder.StateMachineBuilder
import io.github.gaeqs.magicend.ai.statemachine.builder.StateMachineNodeBuilder
import io.github.gaeqs.magicend.ai.tree.builder.TreeNodeBuilder
import io.github.gaeqs.magicend.ai.tree.node.TreeNode
import io.github.gaeqs.magicend.ai.tree.node.TreeNodeNull

class StateMachineNodeTree(
    name: String,
    activity: Activity,
    stateMachine: StateMachine,
    val root: TreeNode,
    val onFail: StateMachineNodeTree.() -> Unit,
    val onSuccess: StateMachineNodeTree.() -> Unit,
) : StateMachineNode(name, activity, stateMachine) {

    private var finished = false

    override fun start() {
        root.start()
        finished = false
    }

    override fun tick() {
        if (finished) return

        val result = root.tick()
        if (result == TreeNode.InvocationResult.WAIT) return

        finished = true
        root.stop()

        when (root.tick()) {
            TreeNode.InvocationResult.FAIL -> onFail(this)
            TreeNode.InvocationResult.SUCCESS -> onSuccess(this)
            else -> {
            }
        }
    }

    override fun stop() {
        if (!finished) {
            root.stop()
        }
    }

    class Builder(
        name: String,
        var root: TreeNodeBuilder<*> = TreeNodeNull.Builder(),
        var onFail: StateMachineNodeTree.() -> Unit = { },
        var onSuccess: StateMachineNodeTree.() -> Unit = { },
    ) : StateMachineNodeBuilder<StateMachineNodeTree>(name) {

        override fun build(activity: Activity, stateMachine: StateMachine) =
            StateMachineNodeTree(name, activity, stateMachine, root.build(activity), onFail, onSuccess)

        fun fail(onFail: StateMachineNodeTree.() -> Unit) {
            this.onFail = onFail
        }

        fun success(onSuccess: StateMachineNodeTree.() -> Unit) {
            this.onSuccess = onSuccess
        }

    }
}

inline fun StateMachineBuilder.tree(name: String, builder: StateMachineNodeTree.Builder.() -> Unit) =
    StateMachineNodeTree.Builder(name).also {
        nodes += it
        builder(it)
    }
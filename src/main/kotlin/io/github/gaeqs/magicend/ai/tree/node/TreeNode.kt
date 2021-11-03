package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity

abstract class TreeNode(val activity: Activity) {

    abstract fun start()

    abstract operator fun invoke(): InvocationResult

    abstract fun stop()

    enum class InvocationResult {
        SUCCESS, FAIL, WAIT;

        fun not() = when (this) {
            SUCCESS -> FAIL
            FAIL -> SUCCESS
            else -> this
        }

    }
}
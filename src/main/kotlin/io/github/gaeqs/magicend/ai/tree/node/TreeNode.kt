package io.github.gaeqs.magicend.ai.tree.node

abstract class TreeNode() {

    abstract fun reset()

    abstract operator fun invoke(): InvocationResult

    enum class InvocationResult {
        SUCCESS, FAIL, WAIT;

        fun not() = when (this) {
            SUCCESS -> FAIL
            FAIL -> SUCCESS
            else -> this
        }

    }
}
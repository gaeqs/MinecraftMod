package io.github.gaeqs.magicend.ai.tree.node

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.Node

/**
 * Represents a [Node] inside a behaviour tree.
 *
 * @param activity the activity of this node.
 */
abstract class TreeNode(activity: Activity) : Node(activity) {

    /**
     * This method is invoked when the node starts its execution.
     * This can be used to initialize data before [tick]
     */
    abstract fun start()

    /**
     * Ticks this node.
     * This method will always be invoked after [start] and before [stop].
     * If you return [InvocationResult.WAIT] this method will be invoked again on the next game tick.
     * To finish this node, return [InvocationResult.SUCCESS] or [InvocationResult.FAIL], depending of
     * the result of the operation.
     *
     * @return the result of the operation.
     */
    abstract fun tick(): InvocationResult

    /**
     * This method is called just after the node finishes its execution.
     * Use this method to clean data.
     */
    abstract fun stop()

    /**
     * Represents the result of a [tick] method's invocation.
     */
    enum class InvocationResult {

        /**
         * Return this if your [tick] method has finished successfully.
         */
        SUCCESS,

        /**
         * Return this if your [tick] method has finished with errors.
         */
        FAIL,

        /**
         * Return this if your [tick] has to be called again in the next game tick.
         */
        WAIT;

        /**
         * @return the inverse of this result. [InvocationResult.WAIT] has no inverse.
         */
        fun not() = when (this) {
            SUCCESS -> FAIL
            FAIL -> SUCCESS
            else -> this
        }

    }
}
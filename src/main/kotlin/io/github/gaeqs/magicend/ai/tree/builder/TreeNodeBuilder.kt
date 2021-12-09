package io.github.gaeqs.magicend.ai.tree.builder

import io.github.gaeqs.magicend.ai.Activity
import io.github.gaeqs.magicend.ai.tree.node.TreeNode

/**
 * A class that will create a node when required.
 */
interface TreeNodeBuilder<T : TreeNode> {

    /**
     * Creates a node and links it to the given [Activity].
     * @param activity the [Activity].
     */
    fun build(activity: Activity): T

}
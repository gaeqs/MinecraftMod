package io.github.gaeqs.magicend.ai

/**
 * This is the base class of a node.
 * Nodes implement simple functionality to the intelligence of an entity.
 *
 * Nodes can have parents and children. Nodes are always children of an [Activity].
 *
 *
 * @param activity the activity of this node.
 */
open class Node(val activity: Activity) {

    /**
     * The artificial intelligence of the activity this node is child of.
     */
    inline val ai get() = activity.ai

    /**
     * The entity the artificial intelligence is controlling.
     */
    inline val entity get() = activity.ai.entity

}
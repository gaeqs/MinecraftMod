package io.github.gaeqs.magicend.ai

open class Node(val activity: Activity) {

    inline val ai get() = activity.ai
    inline val entity get() = activity.ai.entity

}
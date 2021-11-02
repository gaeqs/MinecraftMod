package io.github.gaeqs.magicend.ai

import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*

class Test {


    fun createTree(): TreeNode {
        return loopUnconditional {

            or {
                and {
                    nul()
                    or {
                        and {
                            nul()
                            wait(50)
                        }
                        wait(5)
                    }
                }

                or {
                    and {
                        nul()
                        wait(5)
                    }
                    wait(5)
                }
            }

        }.build()
    }

    fun useTree () {
        val tree = createTree()
        val activity = TreeActivity(tree)
        activity.tick()
    }

}
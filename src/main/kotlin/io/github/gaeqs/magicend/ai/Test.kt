package io.github.gaeqs.magicend.ai

import io.github.gaeqs.magicend.ai.defaults.tree.findNearestLivingEntities
import io.github.gaeqs.magicend.ai.defaults.tree.findRandomWalkTarget
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*

class Test {


    fun createTree(ai: EntityAI): Activity {
        return TreeActivity("test", ai,
            rootLoopUnconditional {
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
                    findRandomWalkTarget(1.0f)
                    findNearestLivingEntities()
                    wait(20)
                }
            }
        )
    }

}
package io.github.gaeqs.magicend.ai

import io.github.gaeqs.magicend.ai.defaults.tree.findNearestLivingEntities
import io.github.gaeqs.magicend.ai.defaults.tree.findWalkTarget
import io.github.gaeqs.magicend.ai.tree.TreeActivity
import io.github.gaeqs.magicend.ai.tree.node.*

class Test {


    fun createTree(ai: EntityAI): Activity {
        return TreeActivity("test", ai,
            loopUnconditional {
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
                    findWalkTarget(1.0f)
                    findNearestLivingEntities()
                    wait(20)
                }
            }
        )
    }

}
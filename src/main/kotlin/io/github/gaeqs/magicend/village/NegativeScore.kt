package io.github.gaeqs.magicend.village

import kotlin.math.max

class NegativeScore(val score: Int, val setOn: Long) {

    val currentScore: Int
        get() {
            val duration = max(System.currentTimeMillis() - setOn, 0) / 3000
            return max(0, score - duration).toInt()
        }

}
package utils

import kotlin.math.abs

data class IntPoint(val x: Int, val y: Int) {

    operator fun plus(other: IntPoint) : IntPoint {
        return IntPoint(x + other.x, y + other.y)
    }
    fun manhattanDistance(other: IntPoint) : Int {
        return abs(other.x - x) + abs(other.y - y)
    }
}

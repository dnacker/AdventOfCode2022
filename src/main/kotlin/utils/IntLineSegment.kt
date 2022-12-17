package utils
data class IntLineSegment(val a: IntPoint, val b: IntPoint) : Iterable<IntPoint> {
    val minX = a.x.coerceAtMost(b.x)
    val maxX = a.x.coerceAtLeast(b.x)
    val minY = a.y.coerceAtMost(b.y)
    val maxY = a.y.coerceAtLeast(b.y)

    fun contains(other: IntPoint) : Boolean {
        return (minX..maxX).contains(other.x) &&
            (minY..maxY).contains(computeY(other.x))
    }

    fun computeY(x: Int) : Int {
        return (slope() * x + yInt()).toInt()
    }
    fun slope() : Double {
        return (b.y.toDouble() - a.y.toDouble()) / (b.x.toDouble() - a.x.toDouble())
    }

    fun yInt() : Double {
        return a.y - slope() * a.x
    }
    fun intersect(other: IntLineSegment) : Set<IntPoint> {
        if (slope() == other.slope()) {
            return buildSet {
                forEach {
                    if (other.contains(it)) {
                        add(it)
                    }
                }
            }
        }

        val x = ((yInt() - other.yInt()) / (other.slope() - slope())).toInt()
        val point = IntPoint(x, computeY(x))
        if (contains(point) && other.contains(point)) {
            return setOf(IntPoint(x, computeY(x)))
        }

        return emptySet()
    }

    override fun iterator(): Iterator<IntPoint> {
        return LineIterator()
    }

    inner class LineIterator : Iterator<IntPoint> {
        private var x = minX
        override fun hasNext(): Boolean {
            return x < maxX
        }

        override fun next(): IntPoint {
            val toReturn = IntPoint(x, computeY(x))
            x++
            return toReturn
        }
    }
}

class Day4(lines: List<String>) : Day(lines) {
    private val rangeList : List<Pair<IntRange, IntRange>> = lines.map { it.asIntRangePair() }

    private infix fun IntRange.contains(other: IntRange) : Boolean {
        return first <= other.first && last >= other.last
    }

    private infix fun IntRange.overlaps(other: IntRange) : Boolean {
        return first <= other.last && last >= other.first
    }

    override fun answerOne(): Any {
        return rangeList.count {
                (first, second) -> first contains second || second contains first
        }
    }

    override fun answerTwo(): Any {
        return rangeList.count {
                (first, second) -> first overlaps second
        }
    }

    private fun String.asIntRangePair(): Pair<IntRange, IntRange> =
        substringBefore(",").asIntRange() to substringAfter(",").asIntRange()

    private fun String.asIntRange(): IntRange =
        substringBefore("-").toInt() .. substringAfter("-").toInt()
}
import java.util.*

class Day12(lines: List<String>) : Day(lines) {
    data class Pos(val row: Int, val col: Int)
    private var start: Pos = Pos(0,0)
    private var end: Pos = Pos(0,0)
    private var width = lines[0].length
    private var height = lines.size

    private val map: List<List<Int>>
    private val visited: MutableMap<Pos, Int> = mutableMapOf()

    init {
        map = lines.mapIndexed { rowIdx, row ->
            row.mapIndexed { colIdx, char ->
                when (char) {
                    'S' -> {
                        start = Pos(rowIdx, colIdx)
                        0
                    }
                    'E' -> {
                        end = Pos(rowIdx, colIdx)
                        25
                    }
                    else -> {
                        char - 'a'
                    }
                }
            }
        }
    }

    private fun bfsFromA(start: Pos, reversed: Boolean) {
        visited.clear()
        val toVisit: Queue<Pos> = LinkedList()
        toVisit.add(start)
        visited[start] = 0
        do {
            val curr = toVisit.poll()
            val candidates = listOf(
                Pos(curr.row - 1, curr.col),
                Pos(curr.row, curr.col + 1),
                Pos(curr.row + 1, curr.col),
                Pos(curr.row, curr.col - 1)
            )
            for (candidate in candidates) {
                if (shouldVisit(curr, candidate, visited, reversed)) {
                    toVisit.add(candidate)
                    val distance = visited.getOrDefault(curr, -1)
                    visited[candidate] = distance + 1
                }
            }
        } while (!toVisit.isEmpty())
    }

    private fun getHeight(pos: Pos) : Int {
        return map[pos.row][pos.col]
    }

    private fun shouldVisit(current: Pos, candidate: Pos, visited: Map<Pos, Int>, reversed: Boolean) : Boolean =
                candidate.row in 0 until height &&
                candidate.col in 0 until width &&
                !visited.containsKey(candidate) &&
                        ((!reversed && getHeight(candidate) <= getHeight(current) + 1) || (reversed && getHeight(candidate) >= getHeight(current) - 1))

    override fun answerOne(): Any {
        bfsFromA(start, false)
        return visited[end] ?: 0
    }

    override fun answerTwo(): Any {
        bfsFromA(end, true)
        return visited[
            visited.keys.filter {
                getHeight(it) == 0
            }.sortedBy {
                visited[it]
            }.first()] ?: 0
    }
}
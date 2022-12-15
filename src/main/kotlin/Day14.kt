class Day14(lines: List<String>) : Day(lines) {

    data class Point(val x: Int, val y: Int)

    enum class Square {
        AIR, ROCK, SAND
    }

    class Grid() {
        var dataMap = mutableMapOf<Point, Square>()
        var minX = 0
        var maxX = 0
        var maxY = 0

        fun reset() {
            dataMap = dataMap.filter { it.value == Square.ROCK }.toMutableMap()
            movingSand = startingSand
        }

        fun addRocks(rocks: List<List<Point>>) {
            rocks.forEach {
                it.zipWithNext { left, right ->
                    if (left.x == right.x) {
                        for (i in left.y.coerceAtMost(right.y)..left.y.coerceAtLeast(right.y)) {
                            insertRock(Point(left.x, i))
                        }
                    } else {
                        for (i in left.x.coerceAtMost(right.x)..left.x.coerceAtLeast(right.x)) {
                            insertRock(Point(i, left.y))
                        }
                    }
                }
            }
        }

        fun insertRock(rock: Point) {
            minX = minX.coerceAtMost(rock.x)
            maxX = maxX.coerceAtLeast(rock.x)
            maxY = maxY.coerceAtLeast(rock.y)
            dataMap[rock] = Square.ROCK
        }

        private val startingSand = Point(500, 0)

        private var movingSand = startingSand


        fun tick() : Boolean {
            val below = Point(movingSand.x, movingSand.y + 1)
            val belowLeft = Point(movingSand.x - 1, movingSand.y + 1)
            val belowRight = Point(movingSand.x + 1, movingSand.y + 1)
            if (below.y > maxY) {
                println("off bottom done!")
                return false
            }
            if (getSquareAt(below) == Square.AIR) {
                movingSand = below
                return true
            }
            if (belowLeft.x < minX) {
                println("off left done!")
                return false
            }
            if (getSquareAt(belowLeft) == Square.AIR) {
                movingSand = belowLeft
                return true
            }
            if (belowRight.x > maxX) {
                println("off right done!")
                return false
            }
            if (getSquareAt(belowRight) == Square.AIR) {
                movingSand = belowRight
                return true
            }

            dataMap[movingSand] = Square.SAND
            if (movingSand == startingSand) {
                println("full up!")
                return false
            }
            movingSand = startingSand
            return true
        }

        fun getSquareAt(point: Point) : Square {
            return dataMap[point] ?: Square.AIR
        }

        fun countSand() : Int {
            return dataMap.values.count { it == Square.SAND }
        }
    }

    val grid: Grid

    fun buildRockList(lines: List<String>) : List<List<Point>> {
        return buildList(lines.size) {
            lines.forEach {
                add(it.split(" -> ").map {
                    val point = it.split(",")
                    val x = point.first().toInt()
                    val y = point.last().toInt()
                    Point(x, y)
                })
            }
        }
    }

    init {
        val rockLists = buildRockList(lines)
        grid = Grid()
        grid.addRocks(rockLists)
    }
    override fun answerOne(): Any {
        while (grid.tick()) {}
        return grid.countSand()
    }

    override fun answerTwo(): Any {
        grid.reset()
        grid.addRocks(buildRockList(listOf("-200,${grid.maxY + 2} -> ${grid.maxX + 200},${grid.maxY + 2}")))
        while (grid.tick()) {}
        return grid.countSand()
    }
}
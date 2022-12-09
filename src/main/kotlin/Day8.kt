
class Day8(lines: List<String>) : Day(lines) {
    class Tree(private val height: Int, var visible: Boolean, var score: Int = 0) {
        operator fun compareTo(other: Tree) : Int = height - other.height
    }

    enum class Dir {
        LEFT, RIGHT, UP, DOWN
    }

    private val width = lines.first().length
    private val height = lines.size
    private val heights = lines.mapIndexed { rowIdx, row ->
        row.mapIndexed { colIdx, char ->
            Tree(char.digitToInt(), isEdge(rowIdx, colIdx))
        }
    }

    init {
        heights.forEachIndexed { rowIdx, trees ->
            trees.forEachIndexed { colIdx, _ ->
                run {
                    processTreeVisibility(rowIdx, colIdx)
                    processTreeScore(rowIdx, colIdx)
                }
            }
        }
    }

    fun isEdge(rowIdx: Int, colIdx: Int) : Boolean {
        return colIdx == 0 ||
                colIdx == width - 1 ||
                rowIdx == 0 ||
                rowIdx == height - 1
    }

    fun isInner(rowIdx: Int, colIdx: Int) : Boolean {
        return rowIdx > 0 &&
                colIdx > 0 &&
                rowIdx < height - 1 &&
                colIdx < width - 1
    }

    fun processTreeVisibility(rowIdx: Int, colIdx: Int) {
        val currTree = heights[rowIdx][colIdx]
        if (currTree.visible) return

        Dir.values().forEach {
            processTreeVisibilityDirection(rowIdx, colIdx, it)
        }
    }

    fun processTreeScore(rowIdx: Int, colIdx: Int) {
        var total = 1
        Dir.values().forEach {
            total *= processScore(rowIdx, colIdx, it)
        }

        heights[rowIdx][colIdx].score = total
    }

    fun processTreeVisibilityDirection(rowIdx: Int, colIdx: Int, dir: Dir) {
        val currTree = heights[rowIdx][colIdx]
        if (currTree.visible) return

        var currRow = rowIdx
        var currCol = colIdx
        while (isInner(currRow, currCol)) {
            when (dir) {
                Dir.LEFT -> currRow--
                Dir.RIGHT -> currRow++
                Dir.UP -> currCol--
                Dir.DOWN -> currCol++
            }
            if (heights[currRow][currCol] >= currTree) {
                currTree.visible = false
                return
            }
        }
        currTree.visible = true
    }

    fun processScore(rowIdx: Int, colIdx: Int, dir: Dir) : Int {
        var score = 0
        doDirectionalOperation(rowIdx, colIdx, dir,
            { currTree, compareTree -> currTree > compareTree },
            { score++ }
        )
        return score
    }

    fun doDirectionalOperation(rowIdx: Int, colIdx: Int, dir: Dir, check: (Tree, Tree) -> Boolean, operation: () -> Unit) {
        val currTree = heights[rowIdx][colIdx]
        var currRow = rowIdx
        var currCol = colIdx
        do {
            operation()
            when (dir) {
                Dir.LEFT -> currCol--
                Dir.RIGHT -> currCol++
                Dir.UP -> currRow--
                Dir.DOWN -> currRow++
            }
        } while (isInner(currRow, currCol) && check(currTree, heights[currRow][currCol]))
    }

    override fun answerOne(): Any {
        return heights.sumOf { list ->
            list.count {
                it.visible
            }
        }
    }

    override fun answerTwo(): Any {
        return heights.flatten().maxWith { left: Tree, right: Tree ->
            left.score - right.score
        }.score
    }
}
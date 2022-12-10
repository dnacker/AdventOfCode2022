class Day9(lines: List<String>) : Day(lines) {
    class Knot(var x: Int = 0, var y: Int = 0)

    var snake = buildList(10) {
        repeat(10) {
            add(Knot())
        }
    }

    val firstVisited = mutableSetOf<Pair<Int, Int>>()
    val lastVisited = mutableSetOf<Pair<Int, Int>>()
    init {
        lines.forEach {
            val dir = it[0]
            val steps = it.substringAfter(" ").toInt()
            for (i in 0 until steps) {
                when (dir) {
                    'L' -> moveLeft()
                    'R' -> moveRight()
                    'U' -> moveUp()
                    'D' -> moveDown()
                }
                firstVisited.add(Pair(snake[1].x, snake[1].y))
                lastVisited.add(Pair(snake.last().x, snake.last().y))
            }
        }


    }

    private fun printTailVisited() {
        val minX = lastVisited.minOf { it.first }
        val minY = lastVisited.minOf { it.second }
        val maxX = lastVisited.maxOf { it.first }
        val maxY = lastVisited.maxOf { it.second }

        for (y in 0 .. maxY - minY) {
            for (x in 0 .. maxX - minX) {
                if (lastVisited.contains(Pair(x + minX, y + minY))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    private fun catchSnakeUp() {
        snake.forEachIndexed { index, head ->
            if (head == snake.last()) return
            catchUp(head, snake[index + 1])
        }
    }

    private fun catchUp(head: Knot, tail: Knot) {
        if (head.x > tail.x + 1) { // move right
            tail.x++
            if (head.y > tail.y) {
                tail.y++
            } else if (head.y < tail.y) {
                tail.y--
            }
        } else if (head.x < tail.x - 1) { // move left
            tail.x--
            if (head.y > tail.y) {
                tail.y++
            } else if (head.y < tail.y) {
                tail.y--
            }
        } else if (head.y > tail.y + 1) { // move up
            tail.y++
            if (head.x > tail.x) {
                tail.x++
            } else if (head.x < tail.x) {
                tail.x--
            }
        } else if (head.y < tail.y - 1) { // move down
            tail.y--
            if (head.x > tail.x) {
                tail.x++
            } else if (head.x < tail.x) {
                tail.x--
            }
        }
    }
    private fun moveLeft() {
        snake.first().x--
        catchSnakeUp()
    }

    private fun moveRight() {
        snake.first().x++
        catchSnakeUp()
    }

    private fun moveUp() {
        snake.first().y++
        catchSnakeUp()
    }

    private fun moveDown() {
        snake.first().y--
        catchSnakeUp()
//        snake.forEachIndexed { index, head ->
//            if (head == snake.last()) return
//            val tail = snake[index + 1]
//            if (tail.y - head.y > 1) {
//                tail.y--
//                tail.x = head.x
//            }
//        }
    }
    override fun answerOne(): Any {
        return firstVisited.size
    }

    override fun answerTwo(): Any {
        printTailVisited()
        return lastVisited.size
    }
}
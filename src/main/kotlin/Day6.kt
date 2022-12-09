class Day6(lines: List<String>) : Day(lines) {
    val data = lines[0]

    fun findIndexOfUniqueCharsOfLength(window: Int) : Int {
        for (start in 0..data.length - window) {
            val match = data.substring(start, start + window)
            if (match.toSet().size == window) {
                return start + window
            }
        }

        return -1
    }

    override fun answerOne(): Any {
        return findIndexOfUniqueCharsOfLength(4)
    }

    override fun answerTwo(): Any {
        return findIndexOfUniqueCharsOfLength(14)
    }
}
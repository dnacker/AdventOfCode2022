
class Day1(lines: List<String>) : Day(lines) {
    private val elfs = ArrayList<Int>()

    init {
        lines.forEach {
            if (it.isEmpty() || elfs.isEmpty()) {
                elfs.add(0)
            } else {
                elfs[elfs.size - 1] = it.toInt() + elfs[elfs.size - 1]
            }
        }
        elfs.sort()
    }

    override fun answerOne(): Any {
        return elfs.last();
    }

    override fun answerTwo(): Any {
        return elfs[elfs.size - 1] + elfs[elfs.size - 2] + elfs[elfs.size - 3]
    }


}
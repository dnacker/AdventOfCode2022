
class Day3(lines: List<String>) : Day(lines) {
    private val rucksacks : List<Pair<String, String>>
    init {
        rucksacks = lines.map {
            val one = it.substring(0, it.length / 2)
            val two = it.substring(it.length / 2)
            Pair(one, two)
        }
    }

    private fun scoreChar(char: Char) : Int {
        if (char.isLowerCase()) {
            return char - 'a' + 1
        }
        return char - 'A' + 27
    }

    private fun scoreGroup(group: List<String>) : Int {
        return group.map(String::toSet)
            .reduce { acc, chars -> acc.intersect(chars) }
            .sumOf(::scoreChar)
    }

    override fun answerOne(): Any {
        return rucksacks.sumOf { scoreGroup(listOf(it.first, it.second)) }
    }

    override fun answerTwo(): Any {
        return lines.chunked(3)
            .sumOf { scoreGroup(it) }
    }

}

class Day2(list: List<String>) : Day(list) {
    private val pairs : List<Pair<Char, Char>>

    init {
        pairs = list.map {
            Pair(it[0], it[2])
        }
    }

    private fun scorePair(pair: Pair<Char, Char>) : Int {
        var score = pair.second - 'X'
        val opponent = pair.first - 'A'
        if (score == opponent) {
            score += 3
        } else if ((score + 2) % 3 == opponent) {
            score += 6
        }

        return score + 1
    }

    private fun scorePairTwo(pair: Pair<Char, Char>) : Int {
        var score = pair.second - 'X'
        val opponent = pair.first - 'A'
        score *= 3
        when (score) {
            0 -> score += (opponent + 2) % 3
            3 -> score += opponent
            6 -> score += (opponent + 1) % 3
        }
        return score + 1
    }

    override fun answerOne(): Any {


        return pairs.sumOf { scorePair(it) }
    }

    override fun answerTwo(): Any {
        return pairs.sumOf { scorePairTwo(it) }
    }
}
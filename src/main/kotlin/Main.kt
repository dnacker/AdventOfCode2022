import utils.FileUtils
import java.lang.Exception

object DayFactory {
    fun create(dayNumber: Int, input: List<String>) : Day {
        return when (dayNumber) {
            1 -> Day1(input)
            2 -> Day2(input)
            3 -> Day3(input)
            4 -> Day4(input)
            5 -> Day5(input)
            6 -> Day6(input)
            7 -> Day7(input)
            8 -> Day8(input)
            9 -> Day9(input)
            10 -> Day10(input)
            11 -> Day11(input)
            12 -> Day12(input)
            13 -> Day13(input)
            14 -> Day14(input)
            else -> throw Exception()
        }
    }
}

fun main(args: Array<String>) {
    val day = 14
    val solution = DayFactory.create(day, FileUtils.readFile(day))
    println("Answer one: \n${solution.answerOne()}")
    println("Answer two: \n${solution.answerTwo()}")
}
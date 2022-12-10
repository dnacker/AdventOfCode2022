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
            else -> throw Exception()
        }
    }
}

fun main(args: Array<String>) {
    val day = 9
    val solution = DayFactory.create(day, FileUtils.readFile(9))
    println("Answer one : ${solution.answerOne()}")
    println("Answer two : ${solution.answerTwo()}")

}
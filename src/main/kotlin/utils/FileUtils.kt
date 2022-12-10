package utils

import java.io.FileReader

object FileUtils {
    fun readFile(day: Int) : List<String> {
        return FileReader("src/main/resources/day$day.txt").readLines()
    }

    fun readTestFile() : List<String> {
        return FileReader("src/main/resources/test.txt").readLines()
    }
}
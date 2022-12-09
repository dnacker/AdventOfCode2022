import java.util.*
import kotlin.collections.ArrayList

class Day5(lines: List<String>) : Day(lines) {
    val stackStrings : MutableList<String> = ArrayList()
    val moves : MutableList<Triple<Int, Int, Int>> = ArrayList()

    init {
        for (i in 1..9) {
            stackStrings.add("")
        }

        for (line in lines) {
            if (line.startsWith("move")) {
                parseMove(line)
            } else if (line.trimStart().startsWith("[")){
                parseStack(line)
            }
        }
    }

    private fun parseMove(line: String) {
        val split = line.split(" ")
        moves.add(Triple(split[1].toInt(), split[3].toInt(), split[5].toInt()))
    }

    private fun parseStack(line: String) {
        val spacing = 4
        for (i in 0 .. line.length / 4) {
            val index = i * spacing + 1
            if (line[index].isLetter()) {
                stackStrings[i] = stackStrings[i] + line[index]
            }
        }
    }

    private fun convertStringToStack(str: String) : Stack<Char> {
        val stack = Stack<Char>()
        for (char in str) {
            stack.add(0, char)
        }
        return stack
    }

    private fun convertStackStringToStacks() : List<Stack<Char>> {
        val stacks = ArrayList<Stack<Char>>()
        for (stack in stackStrings) {
            stacks.add(convertStringToStack(stack))
        }
        return stacks
    }

    private fun getTopOfStacks(stacks: List<Stack<Char>>) : String {
        var result = ""
        for (stack in stacks) {
            when (stack.size) {
                0 -> result += ""
                else -> result += stack.peek()
            }
        }
        return result;
    }

    override fun answerOne(): Any {
        val stacks = convertStackStringToStacks()

        for (move in moves) {
            for (i in 1..move.first.coerceAtMost(stacks[move.second - 1].size)) {
                stacks[move.third - 1].push(stacks[move.second - 1].pop())
            }
        }

        return getTopOfStacks(stacks)
    }

    override fun answerTwo(): Any {
        val stacks = convertStackStringToStacks()

        for (move in moves) {
            val src = stacks[move.second - 1]
            val dest = stacks[move.third - 1]
            val tempstack = Stack<Char>()
            for (i in 1..move.first.coerceAtMost(src.size)) {
                tempstack.push(src.pop())
            }
            while (!tempstack.isEmpty()) {
                dest.push(tempstack.pop())
            }
        }

        return getTopOfStacks(stacks)
    }
}
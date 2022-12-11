import java.lang.StringBuilder

class Day10(lines: List<String>) : Day(lines) {
    class CPU(private val instructions: List<Instruction>, private val state: State = State()) {
        class State(var cycle: Int = 0, var regX: Int = 1)

        companion object {
            fun from(lines: List<String>) : CPU {
                return CPU(lines.map { Instruction.from(it) })
            }
        }

        fun executeInstructions(stateWatcher : (state: State) -> Unit) {
            instructions.forEach {
                it.execute(state, stateWatcher)
            }
        }



        sealed class Instruction(val cycles: Int, val value: Int = 0) {
            companion object {
                fun from(str: String) : Instruction {
                    if (str.startsWith("addx")) {
                        return AddX(str.substringAfter(" ").toInt())
                    }
                    return NoOp()
                }
            }

            fun execute(state: State, stateWatcher : (state: State) -> Unit) {
                repeat(cycles) {
                    state.cycle++
                    stateWatcher.invoke(state)
                }
                doOp(state)
            }

            open fun doOp(state: State) {}
        }
        class NoOp : Instruction(1)
        class AddX(value: Int) : Instruction(2, value) {
            override fun doOp(state: State) {
                state.regX += value
            }
        }
    }

    override fun answerOne(): Any {
        val cpu = CPU.from(lines)
        var sum = 0L
        cpu.executeInstructions { state ->
            if ((state.cycle + 20) % 40 == 0) {
                sum += state.cycle * state.regX
            }
        }
        return sum
    }

    override fun answerTwo(): Any {
        val cpu = CPU.from(lines)
        val result = StringBuilder()
        cpu.executeInstructions { state ->
            if (IntRange(state.regX - 1, state.regX + 1).contains((state.cycle - 1) % 40)) {
                result.append('#')
            } else {
                result.append('.')
            }
            if (state.cycle % 40 == 0) {
                result.append('\n')
            }
        }
        return result.toString()
    }
}
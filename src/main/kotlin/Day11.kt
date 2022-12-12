class Day11(lines: List<String>) : Day(lines) {
    class Monkey(val items: MutableList<Int> = mutableListOf(),
                 val inspectFunction: (Long) -> Long,
                 val monkeyToss: (Int) -> Int,
                 val divisibleTest: Int,
                 var inspectCount : Long = 0) {
        companion object {
            fun from(lines: List<String>) : Monkey {
                val items = lines[1].substringAfter(": ").split(", ").map { it.toInt() }
                val operation = lines[2].substringAfter("= ").split(" ")
                val inspectFunction: (Long) -> Long = {
                    val left = when (operation[0]) {
                        "old" -> it
                        else -> operation[0].toLong()
                    }

                    val right = when (operation[2]) {
                        "old" -> it
                        else -> operation[2].toLong()
                    }

                    if (operation[1] == "*") {
                        left * right
                    } else {
                        left + right
                    }
                }

                val divisible = lines[3].substringAfterLast(" ").toInt()
                val trueTarget = lines[4].substringAfterLast(" ").toInt()
                val falseTarget = lines[5].substringAfterLast(" ").toInt()
                val monkeyToss: (Int) -> Int = {
                    if (it % divisible == 0) {
                        trueTarget
                    } else {
                        falseTarget
                    }
                }
                return Monkey(items.toMutableList(), inspectFunction, monkeyToss, divisible)
            }
        }

        override fun toString() : String {
            return "inspected count: $inspectCount"
        }
    }

    class Monkeys(val monkeys: List<Monkey>, val scary : Boolean = false) {
        private var maxMonkey = 1
        init {
            for (monkey in monkeys) {
                maxMonkey *= monkey.divisibleTest
            }
        }

        fun playRound() {
            for (monkey in monkeys) {
                monkey.inspectCount += monkey.items.size
                while (monkey.items.size > 0) {
                    val item = monkey.items.removeFirst()
                    var inspectedItem = (monkey.inspectFunction(item.toLong()) % maxMonkey).toInt()
                    if (scary) inspectedItem /= 3
                    val targetMonkey = monkey.monkeyToss(inspectedItem)
                    monkeys[targetMonkey].items.add(inspectedItem)
                }
            }
        }
    }

    fun buildMonkeys(scary: Boolean) : Monkeys = Monkeys(buildList {
        lines.chunked(7).forEach {
            this.add(Monkey.from(it))
        }
    }, scary)




    override fun answerOne(): Any {
        val monkeys = Monkeys(lines.chunked(7) {
            Monkey.from(it)
        }, true)
        repeat(20) {
            monkeys.playRound()
        }
        val topTwo = monkeys.monkeys.sortedBy { it.inspectCount }.takeLast(2)
        return topTwo.first().inspectCount * topTwo.last().inspectCount
    }

    override fun answerTwo(): Any {
        val monkeys = Monkeys(lines.chunked(7) {
            Monkey.from(it)
        }, false)
        repeat(10000) {
            monkeys.playRound()
        }
        val topTwo = monkeys.monkeys.sortedBy { it.inspectCount }.takeLast(2)
        return topTwo.first().inspectCount * topTwo.last().inspectCount
    }
}
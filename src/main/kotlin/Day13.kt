import java.util.*

class Day13(lines: List<String>) : Day(lines) {
    sealed class PacketData : Comparable<PacketData> {
        companion object {
            fun from(str: String) : PacketData {
                // base case: number
                val asNum = str.toIntOrNull()
                if (asNum is Int) {
                    return Num(asNum)
                }

                // base list case: comma separated list of zero or more integers
                // [1,2,3]
                // [1]
                // []
                val inner = str.substring(1 until str.length - 1)
                if (inner.isEmpty()) {
                    return PacketList(listOf())
                }

                if (!inner.contains("[")) {
                    return PacketList(inner.split(",").map { from(it) })
                }

                // recursive case: comma separated list of lists or numbers
                // [*,*,...]
                val packetList = mutableListOf<PacketData>()
                val stack = Stack<Char>()
                var startIdx = 0
                while (startIdx < inner.length) {
                    if (inner[startIdx] == '[') {
                        stack.push(inner[startIdx])
                        // add a complete stack
                        var endIdx = startIdx + 1
                        while (!stack.empty()) {
                            if (inner[endIdx] == '[') {
                                stack.push(inner[endIdx])
                            }
                            if (inner[endIdx] == ']') {
                                stack.pop()
                            }
                            endIdx++
                        }
                        packetList.add(from(inner.substring(startIdx, endIdx)))
                        startIdx = endIdx
                    } else if (inner[startIdx].isDigit()) {
                        // consume chars until a comma
                        var endIdx = startIdx
                        while (endIdx < inner.length && inner[endIdx] != ',') {
                            endIdx++
                        }
                        packetList.add(from(inner.substring(startIdx, endIdx)))
                        startIdx = endIdx
                    }
                    // consume the comma
                    startIdx++
                }
                return PacketList(packetList)
            }
        }

        override fun compareTo(other: PacketData): Int {
            if (this is Num && other is Num) {
                return v - other.v
            } else if (this is Num) {
                return PacketList(listOf(this)).compareTo(other)
            } else if (other is Num) {
                return compareTo(PacketList(listOf(other)))
            } else {
                this as PacketList
                other as PacketList
                var leftIdx = 0
                var rightIdx = 0
                while (leftIdx < v.size && rightIdx < other.v.size) {
                    val left = v[leftIdx]
                    val right = other.v[rightIdx]
                    if (left < right) {
                        return -1
                    } else if (left > right) {
                        return 1
                    } else {
                        leftIdx++
                        rightIdx++
                    }
                }

                val pairs = v.zip(other.v)
                for (pair in pairs) {
                    if (pair.first < pair.second) {
                        return -1
                    } else if (pair.second < pair.first) {
                        return 1
                    }
                }

                return v.size - other.v.size
            }
        }

        override fun equals(other: Any?): Boolean {
            return toString() == other.toString()
        }
    }

    class Num(val v: Int) : PacketData() {
        override fun toString(): String {
            return v.toString()
        }
    }

    class PacketList(val v: List<PacketData>) : PacketData() {
        override fun toString(): String {
            return v.toString()
        }
    }

    val packets = lines.chunked(3).map {
        Pair(PacketData.from(it.first()), PacketData.from(it[1]))
    }

    override fun answerOne(): Any {
        var sum = 0
        packets.forEachIndexed { index, pair ->
            if (pair.first < pair.second) {
                sum += index + 1
            }
        }
        return sum
    }

    override fun answerTwo(): Any {
        val flattened = packets.flatMap { listOf(it.first, it.second) }.toMutableList()
        val divOne = PacketData.from("[[2]]")
        val divTwo = PacketData.from("[[6]]")
        flattened.add(divOne)
        flattened.add(divTwo)
        flattened.sort()
        return (flattened.indexOf(divOne) + 1) * (flattened.indexOf(divTwo) + 1)
    }
}
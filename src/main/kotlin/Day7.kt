import kotlin.reflect.KClass

class Day7(lines: List<String>) : Day(lines) {

    val root = Dir("/")
    var currentDir = root

    init {
        parseInput()
    }

    open class File(val name: String, private val size: Long) {
        open fun computeSize() : Long = size

        override fun equals(other: Any?): Boolean {
            return name == (other as File).name
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }

        override fun toString(): String {
            return name
        }
    }

    class Dir(name: String, val files: MutableSet<File> = mutableSetOf(), val parent: Dir? = null) : File(name, 0) {
        var memoizedSize: Long? = null

        override fun computeSize() : Long {
            return if (memoizedSize != null) {
                memoizedSize as Long
            } else {
                memoizedSize = files.sumOf {
                    it.computeSize()
                }
                memoizedSize as Long
            }
        }

    }

    fun visitAllDirectories(visitor: (Dir) -> Unit) {
        val bfsQueue = ArrayDeque<Dir>()
        bfsQueue.add(root)
        while (!bfsQueue.isEmpty()) {
            val curr = bfsQueue.removeFirst()
            visitor.invoke(curr)
            bfsQueue.addAll(curr.files.filterIsInstance<Dir>())
        }
    }

    override fun answerOne(): Any {
        var sum = 0L
        val max = 100000
        visitAllDirectories {
            if (it.computeSize() < max) {
                sum += it.computeSize()
            }
        }

        return sum
    }

    override fun answerTwo(): Any {
        val dirList = mutableListOf<Dir>()
        val amountNeededToFree = root.computeSize() - (70000000 - 30000000)
        visitAllDirectories {
            if (it.computeSize() > amountNeededToFree) {
                dirList.add(it)
            }
        }
        return dirList.sortedWith {
            first, second -> (first.computeSize() - second.computeSize()).toInt()
        }.first()
            .computeSize()
    }

    fun parseInput() {
        for (line in lines) {
            if (line.startsWith("$")) {
                parseCommand(line)
            } else if (line.startsWith("dir")) {
                parseDir(line)
            } else {
                parseFile(line)
            }
        }
    }

    fun parseCommand(line: String) {
        val commands = line.split(" ")
        val command = commands[1]
        if (command == "cd") {
            val target = commands[2]
            currentDir = if (target == "..") {
                currentDir.parent ?: root
            } else {
                currentDir.files.firstOrNull {
                    it.name == target
                } as Dir? ?: root
            }
        }
    }

    fun parseDir(line: String) {
        currentDir.files.add(Dir(name = line.substringAfter(" "), parent = currentDir))
    }

    fun parseFile(line: String) {
        currentDir.files.add(File(line.substringAfter(" "), line.substringBefore(" ").toLong()))
    }

}
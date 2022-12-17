import utils.IntLineSegment
import utils.IntPoint

class Day15(lines: List<String>) : Day(lines) {
    companion object {
        val test = """
            Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            Sensor at x=9, y=16: closest beacon is at x=10, y=16
            Sensor at x=13, y=2: closest beacon is at x=15, y=3
            Sensor at x=12, y=14: closest beacon is at x=10, y=16
            Sensor at x=10, y=20: closest beacon is at x=10, y=16
            Sensor at x=14, y=17: closest beacon is at x=10, y=16
            Sensor at x=8, y=7: closest beacon is at x=2, y=10
            Sensor at x=2, y=0: closest beacon is at x=2, y=10
            Sensor at x=0, y=11: closest beacon is at x=2, y=10
            Sensor at x=20, y=14: closest beacon is at x=25, y=17
            Sensor at x=17, y=20: closest beacon is at x=21, y=22
            Sensor at x=16, y=7: closest beacon is at x=15, y=3
            Sensor at x=14, y=3: closest beacon is at x=15, y=3
            Sensor at x=20, y=1: closest beacon is at x=15, y=3
            """.trimIndent()
    }
    constructor() : this(test.split("\n"))

    val beacons : Set<IntPoint>
    val sensors : Set<IntPoint>
    val closestBeacon : Map<IntPoint, IntPoint>
    var leftMostSensor : IntPoint = IntPoint(Integer.MAX_VALUE,0)
    var rightMostSensor : IntPoint = IntPoint(Integer.MIN_VALUE,0)
    init {
        beacons = mutableSetOf()
        sensors = mutableSetOf()
        closestBeacon = mutableMapOf()
        lines.forEach {
            var currSensor = IntPoint(0, 0)
            it.split(": ").forEachIndexed { index, part ->
                val parts = part.split("at ")[1].split(", ")
                val x = parts[0].split("=")[1].toInt()
                val y = parts[1].split("=")[1].toInt()
                val point = IntPoint(x, y)

                when (index) {
                    0 -> {
                        if (point.x < leftMostSensor.x) {
                            leftMostSensor = point
                        }
                        if (point.x > rightMostSensor.x) {
                            rightMostSensor = point
                        }
                        currSensor = point
                        sensors.add(point)
                    }

                    else -> {
                        beacons.add(point)
                        closestBeacon[currSensor] = point
                    }
                }
            }
        }
    }

    fun manhattanDistance(a: IntPoint, b: IntPoint) : Int {
        return a.manhattanDistance(b)
    }

    fun canPointNotBeBeacon(point: IntPoint) : Boolean {
        if (beacons.contains(point)) return false
        if (sensors.contains(point)) return false

        var found = false
        sensors.forEach {
            val beacon = closestBeacon[it]!!
            if (manhattanDistance(beacon, it) >= manhattanDistance(point, it)) {
                found = true
            }
        }
        return found
    }

    override fun answerOne(): Any {
        val y = 2000000
        var count = 0L
        var minX = leftMostSensor.x - manhattanDistance(leftMostSensor, closestBeacon[leftMostSensor]!!) * 3
        var maxX = rightMostSensor.x + manhattanDistance(rightMostSensor, closestBeacon[rightMostSensor]!!) * 3
        for (x in minX..maxX) {
            val point = IntPoint(x, y)
            if (canPointNotBeBeacon(point)) count++
        }
        return count
    }

    // top, right, bottom, left
    fun makeSetOfPointsInEachDirection(point: IntPoint, distance: Int) : Array<IntPoint> {
        return arrayOf(
            point + IntPoint(0, -distance),
            point + IntPoint(distance, 0),
            point + IntPoint(0, distance),
            point + IntPoint(-distance, 0)
        )
    }

    fun makeSensorLineSegments(sensor: IntPoint) : Set<IntLineSegment> {
        val distanceToBeacon = sensor.manhattanDistance(closestBeacon[sensor]!!)
        val points = makeSetOfPointsInEachDirection(sensor, distanceToBeacon)
        return setOf(
            IntLineSegment(points[0], points[1]),
            IntLineSegment(points[1], points[2]),
            IntLineSegment(points[2], points[3]),
            IntLineSegment(points[3], points[0])
        )
    }

    override fun answerTwo(): Any {
        val intersectionPoints = mutableSetOf<IntPoint>()
        val lineSegments = buildList { 
            sensors.forEach { 
                addAll(makeSensorLineSegments(it))
            }
        }

        val range = 0..4000000
//        val range= 0..20
        val rangeCheck: (IntPoint) -> Boolean = {
            range.contains(it.x) && range.contains(it.y)
        }
        
        for (i in lineSegments.indices) {
            val segment = lineSegments[i]
            for (j in i + 1 until lineSegments.size) {
                val other = lineSegments[j]
                val intersection = segment.intersect(other)
                intersectionPoints.addAll(intersection.filter { rangeCheck(it) })
            }
        }
        
        for (point in intersectionPoints) {
            val neighbies = makeSetOfPointsInEachDirection(point, 1).filter {
                rangeCheck(it)
            }.filterNot { sensors.contains(it) || beacons.contains(it) }
            for (toCheck in neighbies) {
                if (!canPointNotBeBeacon(toCheck)) {
                    return toCheck.x.toLong() * 4000000 + toCheck.y
                }
            }
        }

        return "foo"
    }
}
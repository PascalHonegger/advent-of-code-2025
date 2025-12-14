import java.util.concurrent.atomic.AtomicLong
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val corners = input.map { it.toPosition() }
        var biggestArea = 0L
        for (cornerA in corners) {
            for (cornerB in corners) {
                val width = abs(1L + cornerA.x.toLong() - cornerB.x.toLong())
                val height = abs(1L + cornerA.y.toLong() - cornerB.y.toLong())
                val area = width * height
                biggestArea = max(biggestArea, area)
            }
        }
        return biggestArea
    }

    fun part2(input: List<String>): Long {
        val redTiles = input.map { it.toPosition() }
        val redTileXs = redTiles.map { it.x }.distinct().sorted()
        val redTileYs = redTiles.map { it.y }.distinct().sorted()
        val originalToSquishedPosition = mutableMapOf<Position, Position>()
        val squishedToOriginalPosition = mutableMapOf<Position, Position>()
        redTiles.forEach { original ->
            val squished = Position(redTileXs.indexOf(original.x) * 2, redTileYs.indexOf(original.y) * 2)
            originalToSquishedPosition[original] = squished
            squishedToOriginalPosition[squished] = original
        }
        val squishedRedTiles = squishedToOriginalPosition.keys

        val pointSet = mutableSetOf<Position>()
        squishedToOriginalPosition.keys.forEach { pointSet += it }
        redTiles.forEachIndexed { index, originalTile ->
            val originalNext = redTiles.getOrElse(index + 1) { redTiles.first() }

            val tile = originalToSquishedPosition.getValue(originalTile)
            val next = originalToSquishedPosition.getValue(originalNext)
            if (tile.x == next.x) {
                check(tile.y != next.y)
                for (y in min(tile.y, next.y)..max(tile.y, next.y)) {
                    pointSet += Position(tile.x, y)
                }
            } else {
                check(tile.y == next.y)
                for (x in min(tile.x, next.x)..max(tile.x, next.x)) {
                    pointSet += Position(x, tile.y)
                }
            }
        }

        val start = squishedRedTiles.minWith(compareBy({ it.y }, { it.x })) + Direction.DOWN_RIGHT
        val workingSet = ArrayDeque<Position>().apply { add(start) }
        while (workingSet.isNotEmpty()) {
            val current = workingSet.removeFirst()
            fun checkDirection(d: Direction) {
                val next = current + d
                if (pointSet.add(next)) workingSet += next
            }
            checkDirection(Direction.LEFT)
            checkDirection(Direction.RIGHT)
            checkDirection(Direction.DOWN)
            checkDirection(Direction.UP)
        }

        val biggestArea = AtomicLong()
        squishedRedTiles.parallelStream().forEach { cornerA ->
            squishedRedTiles.parallelStream().forEach { cornerB ->
                val originalCornerA = squishedToOriginalPosition.getValue(cornerA)
                val originalCornerB = squishedToOriginalPosition.getValue(cornerB)
                val width = 1L + abs(originalCornerA.x.toLong() - originalCornerB.x.toLong())
                val height = 1L + abs(originalCornerA.y.toLong() - originalCornerB.y.toLong())
                val area = width * height
                if (area > biggestArea.get()) {
                    for (x in min(cornerA.x, cornerB.x)..max(cornerA.x, cornerB.x)) {
                        for (y in min(cornerA.y, cornerB.y)..max(cornerA.y, cornerB.y)) {
                            if (Position(x, y) !in pointSet) {
                                return@forEach
                            }
                        }
                    }

                    biggestArea.getAndUpdate { max(it, area) }
                }
            }
        }
        return biggestArea.get()
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 50L)
    check(part2(testInput) == 24L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

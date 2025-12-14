import java.util.PriorityQueue
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.jvm.JvmInline
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = readInputAsText(name).lines()

/**
 * Reads text from the given input txt file
 */
fun readInputAsText(name: String): String = Path("src/$name.txt").readText().trim().trim()

/**
 * Calculate product by multiplying all elements together
 */
fun Iterable<Int>.product(): Int = reduce(Int::times)

/**
 * Calculate product by multiplying all elements together
 */
fun Iterable<Long>.product(): Long = reduce(Long::times)


/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println(): Unit = println(this)

/** Converts "1 2  3" to [1, 2, 3] */
fun String.asSpaceSeparatedInts() = split(' ').filter { it.isNotBlank() }.map { it.toInt() }
fun String.asSpaceSeparatedLongs() = split(' ').filter { it.isNotBlank() }.map { it.toLong() }

inline fun <reified T> MutableMap<T, Int>.increase(key: T, by: Int = 1): Int {
    val existingValue = this[key] ?: 0
    val newValue = existingValue + by
    this[key] = newValue
    return newValue
}

inline fun <reified T> MutableMap<T, Long>.increase(key: T, by: Long = 1): Long {
    val existingValue = this[key] ?: 0L
    val newValue = existingValue + by
    this[key] = newValue
    return newValue
}

inline fun <reified T> List<T>.repeated(n: Int): List<T> = buildList {
    repeat(n) {
        addAll(this@repeated)
    }
}

/**
 * Taken from https://www.baeldung.com/kotlin/lcm
 */
fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

/**
 * Safe transpose a list of unequal-length lists.
 *
 * Example:
 * transpose(List(List(1, 2, 3), List(4, 5, 6), List(7, 8)))
 * -> List(List(1, 4, 7), List(2, 5, 8), List(3, 6))
 *
 * Inspired by https://gist.github.com/clementgarbay/49288c006252955c2a3c6139a61ca92a
 */
fun <E> List<List<E>>.transpose(): List<List<E>> {
    // Helpers
    fun <E> List<E>.head(): E = this.first()
    fun <E> List<E>.tail(): List<E> = this.takeLast(this.size - 1)
    fun <E> E.append(xs: List<E>): List<E> = listOf(this).plus(xs)

    return filter { it.isNotEmpty() }.let { ys ->
        if (ys.isNotEmpty()) {
            ys.map { it.head() }.append(ys.map { it.tail() }.transpose())
        } else {
            emptyList()
        }
    }
}

fun List<String>.get2D(x: Int, y: Int): Char? {
    if (y in indices) {
        val row = get(y)
        if (x in row.indices) {
            return row[x]
        }
    }
    return null
}

data class Position(val x: Int, val y: Int) {
    override fun toString(): String = "($x, $y)"
}

data class Position3d(val x: Int, val y: Int, val z: Int) : Comparable<Position3d> {
    override fun toString(): String = "($x, $y, $z)"
    override fun compareTo(other: Position3d): Int {
        return COMPARATOR.compare(this, other)
    }
    companion object {
        private val COMPARATOR =
            Comparator.comparingInt<Position3d> { it.x }
                .thenComparingInt { it.y }
                .thenComparingInt { it.z }
    }
}

fun Position.manhattanDistanceTo(other: Position): Int = abs(x - other.x) + abs(y - other.y)
fun Position3d.euclideanDistanceTo(other: Position3d): Double = sqrt(((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2) + (z - other.z).toDouble().pow(2)).toDouble())

fun String.toPosition(delimiter: String = ","): Position = split(delimiter).let { (x, y) -> Position(x.toInt(), y.toInt()) }
fun String.toPosition3d(delimiter: String = ","): Position3d = split(delimiter).let { (x, y, z) -> Position3d(x.toInt(), y.toInt(), z.toInt()) }

data class Direction(val x: Int, val y: Int) {

    override fun toString(): String = when (this) {
        LEFT -> "<"
        DOWN -> "v"
        RIGHT -> ">"
        UP -> "^"
        else -> "($x, $y)"
    }

    companion object {
        val UP = Direction(0, -1)
        val DOWN = Direction(0, 1)
        val LEFT = Direction(-1, 0)
        val RIGHT = Direction(1, 0)
        val UP_LEFT = Direction(-1, -1)
        val UP_RIGHT = Direction(1, -1)
        val DOWN_LEFT = Direction(-1, 1)
        val DOWN_RIGHT = Direction(1, 1)

        val EIGHT_SURROUNDING_BLOCKS = listOf(
            UP,
            DOWN,
            LEFT,
            RIGHT,
            UP_LEFT,
            UP_RIGHT,
            DOWN_LEFT,
            DOWN_RIGHT,
        )

        fun fromChar(value: Char): Direction = when (value) {
            '<' -> LEFT
            'v' -> DOWN
            '>' -> RIGHT
            '^' -> UP
            else -> throw IllegalArgumentException("Unknown direction: $value")
        }
    }
}

fun Direction.clockwise(): Direction = when (this) {
    Direction.UP -> Direction.RIGHT
    Direction.RIGHT -> Direction.DOWN
    Direction.DOWN -> Direction.LEFT
    Direction.LEFT -> Direction.UP
    else -> error("Unsupported direction to rotate clockwise: $this")
}

fun Direction.counterclockwise(): Direction = when (this) {
    Direction.UP -> Direction.LEFT
    Direction.LEFT -> Direction.DOWN
    Direction.DOWN -> Direction.RIGHT
    Direction.RIGHT -> Direction.UP
    else -> error("Unsupported direction to rotate counterclockwise: $this")
}

typealias PointMap<T> = Map<Position, T>

fun List<String>.toPointMap(): PointMap<Char> = flatMapIndexed { y, row ->
    row.mapIndexed { x, c ->
        Position(x, y) to c
    }
}.toMap()

fun PointMap<Any>.minY() = minOf { it.key.y }
fun PointMap<Any>.maxY() = maxOf { it.key.y }
fun PointMap<Any>.minX() = minOf { it.key.x }
fun PointMap<Any>.maxX() = maxOf { it.key.x }

fun PointMap<Any>.printMap() {
    for (y in minY()..maxY()) {
        for (x in minX()..maxX()) {
            print(get(Position(x, y)))
        }
        kotlin.io.println()
    }
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int, sizeToSwap: Int = 1) {
    require(sizeToSwap > 0)
    repeat(sizeToSwap) {
        val tmp = this[index1 + it]
        this[index1 + it] = this[index2 + it]
        this[index2 + it] = tmp
    }
}

@JvmInline
value class Weight(val value: Int)

operator fun Weight.plus(other: Weight) = Weight(value + other.value)
operator fun Weight.compareTo(other: Weight) = value.compareTo(other.value)

inline fun <T> dijkstraWeights(start: T, getVisitablePositions: (key: T) -> Sequence<Pair<T, Weight>>): Map<T, Weight> {
    val distances = mutableMapOf<T, Weight>().withDefault { Weight(Int.MAX_VALUE) }
    val priorityQueue = PriorityQueue<Pair<T, Weight>>(compareBy { it.second.value })
    val visited = mutableSetOf<Pair<T, Weight>>()

    priorityQueue.offer(start to Weight(0))
    distances[start] = Weight(0)

    while (!priorityQueue.isEmpty()) {
        val (node, currentDist) = priorityQueue.poll()
        if (visited.add(node to currentDist)) {
            getVisitablePositions(node).forEach { (adjacent, weight) ->
                val totalDist = currentDist + weight
                if (totalDist < distances.getValue(adjacent)) {
                    distances[adjacent] = totalDist
                    priorityQueue.offer(adjacent to totalDist)
                }
            }
        }
    }
    return distances
}

inline fun <T> dijkstraPaths(start: T, getVisitablePositions: (key: T) -> Sequence<Pair<T, Weight>>): Pair<Map<T, Weight>, Map<T, List<T>>> {
    val distances = mutableMapOf<T, Weight>().withDefault { Weight(Int.MAX_VALUE) }
    val prevs = mutableMapOf<T, List<T>>().withDefault { emptyList() }
    val priorityQueue = PriorityQueue<Pair<T, Weight>>(compareBy { it.second.value })
    val visited = mutableSetOf<Pair<T, Weight>>()

    priorityQueue.offer(start to Weight(0))
    distances[start] = Weight(0)
    prevs[start] = emptyList()

    while (!priorityQueue.isEmpty()) {
        val (node, currentDist) = priorityQueue.poll()
        if (visited.add(node to currentDist)) {
            getVisitablePositions(node).forEach { (adjacent, weight) ->
                val totalDist = currentDist + weight
                val adjacentDist = distances.getValue(adjacent)
                if (totalDist < adjacentDist) {
                    distances[adjacent] = totalDist
                    prevs[adjacent] = listOf(node)
                    priorityQueue.offer(adjacent to totalDist)
                } else if (totalDist == adjacentDist) {
                    prevs[adjacent] = prevs.getValue(adjacent) + node
                }
            }
        }
    }
    return Pair(distances, prevs)
}


inline fun <T> Iterable<T>.sumOfIndexed(transform: (index: Int, T) -> Int): Int = mapIndexed(transform).sum()

operator fun Position.plus(direction: Direction): Position =
    Position(x = x + direction.x, y = y + direction.y)

val IntRange.simpleSize: Int get() = last - first + 1
infix fun LongRange.isIncludedIn(other: LongRange): Boolean = first >= other.first && last <= other.last

/**
 * Inspired by https://1gravityllc.medium.com/trie-kotlin-50d8ae041202
 */
class Trie<Value> {
    data class Node<Value>(
        val children: MutableMap<Char, Node<Value>> = mutableMapOf(),
        var value: Value? = null,
    )

    val root = Node<Value>()

    fun insert(key: String, value: Value, node: Node<Value> = root) {
        key.fold(node) { node, char ->
            node.children[char] ?: Node<Value>().also { node.children[char] = it }
        }.value = value
    }

    fun search(key: String, node: Node<Value> = root): Value? = key.fold(node) { node, char ->
        node.children[char] ?: return null
    }.value

    fun delete(key: String) {
        delete(key, 0, root)
    }

    private fun delete(key: String, index: Int, node: Node<Value>) {
        if (index == key.length)
            node.value = null
        else node.children[key[index]]?.run {
            delete(key, index + 1, this)
            if (children.isEmpty() && value == null) node.children.remove(key[index])
        }
    }
}

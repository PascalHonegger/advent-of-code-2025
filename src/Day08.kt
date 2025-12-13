fun main() {
    fun List<String>.toCircuitMap(): Pair<MutableMap<Position3d, MutableSet<Position3d>>, MutableMap<Pair<Position3d, Position3d>, Double>> {
        val positions = map { it.toPosition3d() }
        val distances = mutableMapOf<Pair<Position3d, Position3d>, Double>()
        positions.forEach { pos1 ->
            positions.forEach { pos2 ->
                if (pos1 == pos2) return@forEach
                val key = if (pos1 < pos2) {
                    pos1 to pos2
                } else {
                    pos2 to pos1
                }
                distances.computeIfAbsent(key) { it.first.euclideanDistanceTo(it.second) }
            }
        }
        val circuitsPointer = mutableMapOf<Position3d, MutableSet<Position3d>>()
        positions.forEach { circuitsPointer[it] = mutableSetOf(it) }
        return circuitsPointer to distances
    }

    fun part1(input: List<String>, numConnections: Int): Long {
        val (circuitsPointer, distances) = input.toCircuitMap()

        repeat(numConnections) {
            val closestPair = distances.minBy { it.value }
            distances.remove(closestPair.key)
            val (left, right) = closestPair.key
            val circuitLeft = circuitsPointer.getValue(left)
            val circuitRight = circuitsPointer.getValue(right)
            if (circuitLeft == circuitRight) return@repeat
            circuitLeft.addAll(circuitRight)
            circuitRight.forEach { circuitsPointer[it] = circuitLeft }
        }
        return circuitsPointer.values.distinct().map { it.size.toLong() }.sortedByDescending { it }.take(3).reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val (circuitsPointer, distances) = input.toCircuitMap()

        while (true) {
            val closestPair = distances.minBy { it.value }
            distances.remove(closestPair.key)
            val (left, right) = closestPair.key
            val circuitLeft = circuitsPointer.getValue(left)
            val circuitRight = circuitsPointer.getValue(right)
            if (circuitLeft == circuitRight) continue
            circuitLeft.addAll(circuitRight)
            circuitRight.forEach { circuitsPointer[it] = circuitLeft }
            if (circuitsPointer.values.distinct().size == 1) {
                return left.x.toLong() * right.x.toLong()
            }
        }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput, numConnections = 10) == 40L)
    check(part2(testInput) == 25272L)

    val input = readInput("Day08")
    part1(input, numConnections = 1000).println()
    part2(input).println()
}

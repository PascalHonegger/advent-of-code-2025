fun main() {
    fun removeRolls(map: PointMap<Char>): Pair<PointMap<Char>, Int> {
        val toBeRemoved = map.filter { (point, value) ->
            value == '@' && Direction.EIGHT_SURROUNDING_BLOCKS.count { d -> map[point + d] == '@' } < 4
        }
        val newMap = map.toMutableMap()
        toBeRemoved.keys.forEach { newMap[it] = '.' }
        return Pair(newMap, toBeRemoved.size)
    }

    fun part1(input: List<String>): Int {
        return removeRolls(input.toPointMap()).second
    }

    fun part2(input: List<String>): Int {
        var map = input.toPointMap()
        var totalRemoved = 0
        while (true) {
            val res = removeRolls(map)
            if (res.second == 0)
                return totalRemoved
            map = res.first
            totalRemoved += res.second
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 43)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

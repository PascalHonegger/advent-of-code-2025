fun main() {
    fun parseInput(input: List<String>): Pair<List<LongRange>, List<Long>> {
        val middle = input.indexOfFirst { it.isBlank() }
        val ranges = input.take(middle).map { it.split('-').let { (start, end) -> start.toLong()..end.toLong() } }
        val ids = input.drop(middle + 1).map { it.toLong() }
        return ranges to ids
    }

    fun part1(input: List<String>): Int {
        val (ranges, ids) = parseInput(input)
        return ids.count { id -> ranges.any { id in it } }
    }

    fun part2(input: List<String>): Long {
        val (ranges) = parseInput(input)
        val items = ranges.sortedWith(compareBy<LongRange> { it.first }.thenBy { it.last })
        // look for same starts and only keep the one with the longest end
        val deduplicatedStarts = items.groupBy { it.first }.map { (key, value) -> key..value.maxOf { it.last } }
        // remove ranges included within larger range
        val noSubRanges = deduplicatedStarts.filter { range ->
            deduplicatedStarts.none { otherRange -> range != otherRange && range isIncludedIn otherRange }
        }
        return noSubRanges.mapIndexed { index, current ->
            val next = noSubRanges.getOrNull(index + 1)
            if (next != null) {
                1L + current.last.coerceAtMost(next.first - 1) - current.first
            } else {
                1L + current.last - current.first
            }
        }.sum()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 14L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

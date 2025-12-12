fun main() {
    fun part1(input: List<String>): Int {
        var splits = 0
        val lasers = mutableSetOf(input.first().indexOf('S'))
        input.drop(1).forEach { row ->
            lasers.toSet().forEach { l ->
                if (row[l] == '^') {
                    splits++
                    lasers.remove(l)
                    lasers.add(l - 1)
                    lasers.add(l + 1)
                }
            }
        }
        return splits
    }

    fun part2(input: List<String>): Long {
        val lasers = mutableMapOf(input.first().indexOf('S') to 1L)
        input.drop(1).forEach { row ->
            lasers.keys.toSet().forEach { l ->
                if (row[l] == '^') {
                    val num = checkNotNull(lasers.remove(l))
                    lasers.increase(l - 1, num)
                    lasers.increase(l + 1, num)
                }
            }
        }
        return lasers.values.sum()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 40L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

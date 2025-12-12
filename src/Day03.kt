fun main() {
    fun solve(input: List<String>, digits: Int): Long {
        return input.sumOf { bank ->
            var runningBank = bank
            buildString {
                for (remainingDigits in (digits - 1) downTo 0) {
                    val digit = runningBank.dropLast(remainingDigits).max()
                    append(digit)
                    runningBank = runningBank.drop(runningBank.indexOf(digit) + 1)
                }
            }.toLong()
        }
    }

    fun part1(input: List<String>): Long {
        return solve(input, 2)
    }

    fun part2(input: List<String>): Long {
        return solve(input, 12)
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 357L)
    check(part2(testInput) == 3121910778619L)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

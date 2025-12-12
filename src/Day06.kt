fun main() {
    fun runCalculations(mathOperations: List<Pair<Char, List<Long>>>): Long {
        return mathOperations.sumOf { (operator, operands) ->
            val operation: Long.(other: Long) -> Long = when (operator) {
                '+' -> Long::plus
                '*' -> Long::times
                else -> error("Unknown operation $operator")
            }
            operands.reduce(operation)
        }
    }

    fun part1(input: List<String>): Long {
        val operands = input.dropLast(1).map { it.asSpaceSeparatedLongs() }.transpose()
        val operators = input.last().split("\\s+".toRegex()).map { it.single() }
        return runCalculations(operators.zip(operands))
    }

    fun part2(input: List<String>): Long {
        val columns = input
            .map { it.toCollection(mutableListOf()) }
            .transpose()
            .reversed()
        val mathOperations = buildList {
            val operands = mutableListOf<Long>()
            columns.forEach { column ->
                operands += column.mapNotNull { it.digitToIntOrNull() }.joinToString("").toLongOrNull() ?: return@forEach
                if (column.last() in listOf('+', '*')) {
                    add(Pair(column.last(), operands.toList()))
                    operands.clear()
                }
            }
        }
        return runCalculations(mathOperations)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 4277556L)
    check(part2(testInput) == 3263827L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

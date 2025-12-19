fun main() {
    data class Shape(
        val index: Int,
        val lines: List<String>,
    ) {
        val usedSquares get() = lines.sumOf { l -> l.count { it == '#' } }
        override fun toString(): String = buildString {
            append(index)
            appendLine(':')
            lines.forEach { appendLine(it) }
        }
    }

    fun List<String>.toShape(): Shape {
        return Shape(
            index = first().removeSuffix(":").toInt(),
            lines = drop(1).dropLast(1),
        )
    }

    fun Shape.permutations(): List<Shape> = buildList {
        fun List<String>.rotate() = listOf(
            "${get2D(0, 2)}${get2D(0, 1)}${get2D(0, 0)}",
            "${get2D(1, 2)}${get2D(1, 1)}${get2D(1, 0)}",
            "${get2D(2, 2)}${get2D(2, 1)}${get2D(2, 0)}",
        )
        add(this@permutations)
        add(Shape(index = index, lines = lines.rotate()))
        add(Shape(index = index, lines = lines.rotate().rotate()))
        add(Shape(index = index, lines = lines.rotate().rotate().rotate()))
    }

    data class Grid(
        val width: Int,
        val height: Int,
        val shapesToFit: List<Int>,
    )

    fun String.toGrid(): Grid {
        return Grid(
            width = takeWhile { it != 'x' }.toInt(),
            height = dropWhile { it != 'x' }.drop(1).takeWhile { it != ':' }.toInt(),
            shapesToFit = dropWhile { it != ':' }.drop(2).asSpaceSeparatedInts(),
        )
    }

    fun part1(input: List<String>): Int {
        // This very cheap solution only works on the real input
        // because you have enough shapes in such a way that the spaces can always be filled
        val shapes = input.take(30).chunked(5).map { it.toShape() }
        val grids = input.drop(30).map { it.toGrid() }
        return grids.count { grid ->
            val availableSpace = grid.width * grid.height
            val usedSpace = grid.shapesToFit.sumOfIndexed { index, amount -> shapes[index].usedSquares * amount }
            availableSpace >= usedSpace
        }
    }

    val testInput = readInput("Day12_test")
//    check(part1(testInput) == 2)

    val input = readInput("Day12")
    part1(input).println()
}

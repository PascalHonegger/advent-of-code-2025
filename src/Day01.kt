fun main() {
    data class Rotation(val direction: Char, val amount: Int)
    class Dial {
        private var orientation: Int = 50
        var passedZeroes: Int = 0
        var landedOnZeroes: Int = 0
        operator fun plusAssign(rotation: Rotation) {
            // Very ugly but it's too early for my brain to fix it
            if (orientation == 0 && rotation.direction == 'L') passedZeroes--
            if (rotation.direction == 'L') {
                orientation -= rotation.amount
            } else {
                orientation += rotation.amount
            }
            while (orientation > 100) {
                orientation -= 100
                passedZeroes++
            }
            while (orientation < 0) {
                orientation += 100
                passedZeroes++
            }
            if (orientation == 0 || orientation == 100) {
                passedZeroes++
                orientation = 0
            }
            if (orientation == 0) {
                landedOnZeroes++
            }
        }
    }

    fun part1(input: List<String>): Int {
        val rotations = input.map { Rotation(it.first(), it.drop(1).toInt()) }
        val dial = Dial()
        for (rotation in rotations) {
            dial += rotation
        }
        return dial.landedOnZeroes
    }

    fun part2(input: List<String>): Int {
        val rotations = input.map { Rotation(it.first(), it.drop(1).toInt()) }
        val dial = Dial()
        for (rotation in rotations) {
            dial += rotation
        }
        return dial.passedZeroes
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

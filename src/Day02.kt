fun main() {
    fun String.toIdRanges(): List<LongRange> = split(',')
        .map { it.split('-').let { (first, last) -> first.toLong()..last.toLong() } }

    fun LongRange.extractInvalidIdsOfGroupSizes(groupSizes: List<Int>): List<Long> {
        val res = mutableListOf<Long>()
        numLoop@for (num in this) {
            val asText = num.toString()
            groupLoop@for (groups in groupSizes) {
                if (asText.length % groups != 0) continue@groupLoop
                val blockSize = (asText.length / groups)
                indexLoop@for (idx in 0..<blockSize) {
                    if ((1..<groups).any { asText[idx] != asText[idx + it * blockSize] })
                        continue@groupLoop
                }
                res += num
                continue@numLoop
            }
        }
        return res
    }

    fun part1(input: String): Long {
        return input.toIdRanges().sumOf { it.extractInvalidIdsOfGroupSizes(listOf(2)).sum() }
    }

    fun part2(input: String): Long {
        return input.toIdRanges().sumOf { it.extractInvalidIdsOfGroupSizes(listOf(2, 3, 5, 7)).sum() }
    }

    val testInput = readInputAsText("Day02_test")
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)

    val input = readInputAsText("Day02")
    part1(input).println()
    part2(input).println()
}

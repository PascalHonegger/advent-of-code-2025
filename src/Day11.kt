fun main() {
    data class Device(
        val name: String
    ) {
        val connectedTo: MutableList<Device> = mutableListOf()
    }

    fun List<String>.toDevices(): Map<String, Device> {
        val devices = map { it.split(':').first() }.plus("out").associateWith { Device(it) }
        forEach { line ->
            val device = line.split(':').first()
            val connectedTo = line.split(':').last().split(' ').filter { it.isNotEmpty() }
            devices.getValue(device).connectedTo += connectedTo.map(devices::getValue)
        }
        return devices
    }

    fun part1(input: List<String>): Int {
        val devices = input.toDevices()
        var countToOut = 0
        val visited = mutableSetOf<Device>()
        fun recurse(next: Device) {
            if (next.name == "out") {
                countToOut++
                return
            }
            if (visited.add(next)) {
                next.connectedTo.forEach(::recurse)
                visited.remove(next)
            }
        }
        recurse(devices.getValue("you"))
        return countToOut
    }

    fun part2(input: List<String>): Long {
        val devices = input.toDevices()
        val svr = devices.getValue("svr")
        val dac = devices.getValue("dac")
        val fft = devices.getValue("fft")
        val out = devices.getValue("out")
        fun findPaths(from: Device, to: Device): Long {
            var countToOut = 0L
            val workingSet = mutableMapOf(from to 1L)
            while (workingSet.isNotEmpty()) {
                val (next, amount) = workingSet.asSequence().first()
                workingSet.remove(next)
                if (next == to) {
                    countToOut += amount
                } else {
                    next.connectedTo.forEach { workingSet.increase(it, amount) }
                }
            }
            return countToOut
        }
        val svrToDac = findPaths(svr, dac)
        val svrToFft = findPaths(svr, fft)
        val dacToFft = findPaths(dac, fft)
        val fftToDac = findPaths(fft, dac)
        val dacToOut = findPaths(dac, out)
        val fftToOut = findPaths(fft, out)
        return (svrToDac * dacToFft * fftToOut) + (svrToFft * fftToDac * dacToOut)
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 5)
    check(part2(readInput("Day11_test2")) == 2L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

import org.chocosolver.solver.Model

fun main() {
    class MachineConfiguration(
        val indicatorLights: List<Boolean>,
        val requirements: List<Int>,
        val wiringSchematics: List<List<Int>>,
    )

    fun String.toMachineConfiguration(): MachineConfiguration {
        val indicators = substring(indexOf('[')..indexOf(']'))
        val requirements = substring(indexOf('{')..indexOf('}'))
        val wiring = removeSurrounding(indicators, requirements)
        return MachineConfiguration(
            indicatorLights = indicators.removeSurrounding("[", "]").map { it == '#' },
            requirements = requirements.removeSurrounding("{", "}").split(',').map { it.toInt() },
            wiringSchematics = wiring.trim().split(' ').map { it.removeSurrounding("(", ")").split(',').map { num -> num.toInt() } },
        )
    }

    fun part1(input: List<String>): Int {
        val configs = input.map { it.toMachineConfiguration() }
        return configs.sumOf { config ->
            val weights = dijkstraWeights(start = List(config.indicatorLights.size) { false }) { state ->
                config.wiringSchematics.asSequence().map { toggles ->
                    val newState = state.toMutableList()
                    toggles.forEach { newState[it] = !newState[it] }
                    newState to Weight(1)
                }
            }
            weights.getValue(config.indicatorLights).value
        }
    }

    fun part2(input: List<String>): Int {
        val configs = input.map { it.toMachineConfiguration() }
        return configs.withIndex().toList().parallelStream().map { (index, config) ->
            /*
            Example: (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
                      a    b    c    d     e     f
            e + f     = 3
            b + f     = 5
            c + d + e = 4
            a + b + d = 7
            Optimize for minimal sum(a + b + c + d + e + f)
             */

            val model = Model("Solve config $index")
            val intVars = config.wiringSchematics.mapIndexed { index, schematic -> model.intVar("Schematic #$index", 0, schematic.minOf { config.requirements[it] }) }
            val sumOfVariables = model.sum("Sum of schematics", *intVars.toTypedArray())

            config.requirements.forEachIndexed { index, requirement ->
                val vars = config.wiringSchematics.zip(intVars).filter { index in it.first }.map { it.second }.toTypedArray()
                model.sum("Sum of requirement #$index", *vars).eq(requirement).post()
            }

            val solution = model.solver.findOptimalSolution(sumOfVariables, false)
            solution.getIntVal(sumOfVariables)
        }.toList().sum()
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 33)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

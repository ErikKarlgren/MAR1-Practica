package ucm.erikkarl
/*
import java.io.File
import kotlin.math.log2
import kotlin.math.max

class RandomTestCasesCreator(private val numberOfCases: Int, private val nodesPerGraph: Int) {

    private fun nodesRange(): IntRange {
        return (1..nodesPerGraph)
    }

    fun createTests(fileName: String) {
        val text = StringBuilder()
        for (i in 0 until numberOfCases) {
            createRandomTest(text)
        }
        File(fileName).writeText(text.toString())
    }

    private fun createRandomTest(text: StringBuilder) {
        for (i in nodesRange()) {
            val adjacentNodesText = StringBuilder()
            val adjacentNodes = randomQuantityOfRandomNumbers(nodesRange())
            adjacentNodes.forEach { adjacentNodesText.append("$it ") }

            text.append("$i\n")
            text.append("${adjacentNodesText.toString().trim()}\n")
        }
        text.append("\n")
    }

    private fun randomQuantityOfRandomNumbers(range: IntRange): Set<Int> {
        val usedNumbers: MutableSet<Int> = mutableSetOf()
        val unusedNumbers: MutableSet<Int> = range.toMutableSet()
        val maxQuantity = max(log2(range.max()!!.toDouble()).toInt() / 2, 1)
        val quantity = (1..maxQuantity).random()
        // val quantity = 1

        for (i in 0 until quantity) {
            val n = unusedNumbers.random()
            unusedNumbers.remove(n)
            usedNumbers.add(n)
        }
        return usedNumbers
    }
}*/
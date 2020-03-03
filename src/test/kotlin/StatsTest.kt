import gov.cdc.nccdphp.esurveillance.utils.StatisticsUtils
import org.junit.jupiter.api.Test
import java.util.*

class StatsTest {


    inline fun <reified T> modeOf(a: Array<T>): Array<T> {
        val sortedByFreq = a.groupBy { it }.entries.sortedByDescending { it.value.size }
        val maxFreq = sortedByFreq.first().value.size
        val modes = sortedByFreq.takeWhile { it.value.size == maxFreq }.map { e -> e.key}
        return modes.toTypedArray()
    }

    private fun <T> printMode(modes: Array<T>) {
        if (modes.size == 1)
            println("The mode of the collection is ${modes.first()}  ")
        else {
            print("There are ${modes.size} modes  namely : ")
            println(modes.map { it }.joinToString(", "))
        }
    }
    @Test
    fun testMode() {
        val a = arrayOf(7, 7, 1, 6, 2, 4, 2, 4, 2, 7, 5)
        println("[" + a.joinToString(", ") + "]")
        val modesA = modeOf(a)
        printMode(modesA)

        val b = arrayOf(true, false, true, false, true, true)
        println("[" + b.joinToString(", ") + "]")
        val modesB =  modeOf(b)
        printMode(modesB)
    }

    @Test
    fun testStatsUtilModeOf() {
        val i = listOf(7, 7, 1, 6, 2, 4, 2, 4, 2, 7, 5)
        val modesI = StatisticsUtils.modeOf(i)
        printMode(modesI.toTypedArray())

        val a = listOf("AA", "VV", "AA", "CC", "BB","AA")
        val modesA = StatisticsUtils.modeOf(a)
        printMode(modesA.toTypedArray())

        val d = listOf(Date(), Date(), Date())
        val modesD = StatisticsUtils.modeOf(d)
        printMode(modesD.toTypedArray())

        val b = arrayOf(true, false, true, false, true, true)
        val modesB = StatisticsUtils.modeOf(listOf(b))
        printMode(modesB.toTypedArray())
    }
}
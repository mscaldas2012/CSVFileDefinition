package gov.cdc.nccdphp.esurveillance.utils

class StatisticsUtils {

    companion object {

        fun stdDevOf(array: List<Int>): Double {
            var standardDeviation = 0.0
            val mean = (array.sum() * 1.0)/ array.size

            for (num in array) {
                standardDeviation += Math.pow(num - mean, 2.0)
            }

            return Math.sqrt(standardDeviation / array.size)
        }

         fun <T> modeOf(a: List<T>): List<T> {
             val sortedByFreq = a.groupBy { it }.entries.sortedByDescending { it.value.size }
             if (sortedByFreq.size > 0) {
                 val maxFreq = sortedByFreq.first().value.size
                 return sortedByFreq.takeWhile { it.value.size == maxFreq }.map { e -> e.key }
             }
             return emptyList()
        }

        fun rangeOf(intArray: List<Int>): Int {
            if (intArray.size > 0) {
                return intArray.max()!! - intArray.min()!!
            }
            return 0


        }

    }
}
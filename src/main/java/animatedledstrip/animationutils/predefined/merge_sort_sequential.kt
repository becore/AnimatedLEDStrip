package animatedledstrip.animationutils.predefined

import animatedledstrip.animationutils.Animation
import animatedledstrip.animationutils.ParamUsage
import animatedledstrip.animationutils.PredefinedAnimation
import animatedledstrip.colors.PreparedColorContainer
import animatedledstrip.utils.delayBlocking

@Suppress("DuplicatedCode")
val mergeSortSequential = PredefinedAnimation(
    Animation.AnimationInfo(
        name = "Merge Sort (Sequential)",
        abbr = "MSP",
        repetitive = false,
        minimumColors = 1,
        center = ParamUsage.NOTUSED,
        delay = ParamUsage.USED,
        delayDefault = 25,
        direction = ParamUsage.NOTUSED,
        distance = ParamUsage.NOTUSED,
        spacing = ParamUsage.NOTUSED
    )
) { leds, data, _ ->

    data class SortablePixel(val finalLocation: Int, val currentLocation: Int, val color: Long)

    val colorMap = data.pCols[0].colors.mapIndexed { index, it -> Pair(index, it) }.shuffled()
        .mapIndexed { index, it -> SortablePixel(it.first, index, it.second) }.toMutableList()
    val color = PreparedColorContainer(colorMap.map { it.color })

    leds.apply {
        setProlongedStripColor(color)

        fun updateColorAtLocation(location: Int) {
            setProlongedPixelColor(location, colorMap[location].color)
        }

        fun sort(startIndex: Int, endIndex: Int) {
            if (startIndex == endIndex) return

            val midpoint = startIndex + ((endIndex - startIndex) / 2)
            sort(startIndex, midpoint)
            sort(midpoint + 1, endIndex)

            var p1 = startIndex
            var p2 = midpoint + 1
            for (x in 0 until endIndex - startIndex) {
                when {
                    colorMap[p1].finalLocation < colorMap[p2].finalLocation -> {
                        p1++
                    }
                    colorMap[p2].finalLocation < colorMap[p1].finalLocation -> {
                        val temp = colorMap[p2]
                        for (i in p2 downTo p1 + 1) {
                            colorMap[i] = colorMap[i - 1]
                            updateColorAtLocation(i)
                        }
                        colorMap[p1] = temp
                        updateColorAtLocation(p1)
                        p1++
                        if (p2 < endIndex) p2++
                        delayBlocking(data.delay)
                    }
                }
            }
        }
        sort(0, colorMap.lastIndex)
    }
}

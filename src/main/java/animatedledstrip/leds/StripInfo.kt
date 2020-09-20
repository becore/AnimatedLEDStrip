/*
 *  Copyright (c) 2018-2020 AnimatedLEDStrip
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package animatedledstrip.leds

import animatedledstrip.animationutils.definedAnimationNames
import animatedledstrip.utils.SendableData

/**
 * Data class for setting properties of a LED strip.
 * (Done this way to simplify device libraries so they don't break if
 * more properties are added)
 *
 * @property numLEDs Number of LEDs in the strip
 * @property pin Physical pin the strip is connected to
 * @property imageDebugging If image debugging should be enabled
 * @property fileName File to write image debugging output to
 * @property rendersBeforeSave Renders between image debugging writes
 * @property threadCount Number of threads used by animations
 */
data class StripInfo(
    val numLEDs: Int = 0,
    val pin: Int? = null,
    val imageDebugging: Boolean = false,
    val fileName: String? = null,
    val rendersBeforeSave: Int? = null,
    val threadCount: Int = 100,
) : SendableData {

    companion object {
        const val prefix = "SINF"
    }

    override val prefix = StripInfo.prefix

    val supportedAnimations
        get() = definedAnimationNames

    override fun toHumanReadableString() =
        """
            Strip Info
              numLEDs: $numLEDs
              pin: $pin
              imageDebugging: $imageDebugging
              fileName: $fileName
              rendersBeforeSave: $rendersBeforeSave
              threadCount: $threadCount
              supportedAnimations: $supportedAnimations
            End Strip Info
        """.trimIndent()
}
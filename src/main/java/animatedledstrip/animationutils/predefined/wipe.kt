/*
 *  Copyright (c) 2020 AnimatedLEDStrip
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

package animatedledstrip.animationutils.predefined

import animatedledstrip.animationutils.Animation
import animatedledstrip.animationutils.Direction
import animatedledstrip.animationutils.ParamUsage
import animatedledstrip.animationutils.PredefinedAnimation
import animatedledstrip.leds.iterateOverPixels
import animatedledstrip.leds.iterateOverPixelsReverse
import animatedledstrip.utils.delayBlocking

val wipe = PredefinedAnimation(
    Animation.AnimationInfo(
        name = "Wipe",
        abbr = "WIP",
        description = "Similar to a [Pixel Run](Pixel-Run) animation, but the " +
                      "pixels do not revert to their prolonged color.",
        signatureFile = "wipe.png",
        repetitive = false,
        minimumColors = 1,
        unlimitedColors = false,
        center = ParamUsage.NOTUSED,
        delay = ParamUsage.USED,
        delayDefault = 10,
        direction = ParamUsage.USED,
        distance = ParamUsage.NOTUSED,
        spacing = ParamUsage.NOTUSED,
    )
) { leds, data, _ ->
    val color0 = data.pCols[0]
    val delay = data.delay
    val direction = data.direction

    leds.apply {
        when (direction) {
            Direction.FORWARD ->
                iterateOverPixels {
                    setProlongedPixelColor(it, color0)
                    delayBlocking(delay)
                }
            Direction.BACKWARD -> {
                iterateOverPixelsReverse {
                    setProlongedPixelColor(it, color0)
                    delayBlocking(delay)
                }
            }
        }
    }
}
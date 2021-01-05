/*
 * Copyright (c) 2018-2021 AnimatedLEDStrip
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package animatedledstrip.animations.predefined

import animatedledstrip.animations.Animation
import animatedledstrip.animations.AnimationParameter
import animatedledstrip.animations.Dimensionality
import animatedledstrip.animations.PredefinedAnimation
import animatedledstrip.leds.animationmanagement.numLEDs
import animatedledstrip.leds.colormanagement.setPixelProlongedColor
import animatedledstrip.leds.stripmanagement.PixelLocation
import animatedledstrip.leds.stripmanagement.PixelLocationManager
import animatedledstrip.leds.stripmanagement.transformLocations
import kotlinx.coroutines.delay

val wipe = PredefinedAnimation(
    Animation.AnimationInfo(
        name = "Wipe",
        abbr = "WIP",
        description = "Starts two [Meteor](Meteor) animations running in opposite " +
                      "directions from `center`, stopping after traveling `distance` " +
                      "or at the end of the strip/section, whichever comes first.\n" +
                      "Does not wait for the Meteor animations to be complete before " +
                      "returning, giving a ripple-like appearance when run continuously.",
        signatureFile = "wipe.png",
        runCountDefault = 1,
        minimumColors = 1,
        unlimitedColors = false,
        dimensionality = Dimensionality.anyDimensional,
        directional = false,
        intParams = listOf(AnimationParameter("interMovementDelay", "Delay between movements in the animation", 30)),
        doubleParams = listOf(AnimationParameter("movementPerIteration",
                                                 "How far to move during each iteration of the animation",
                                                 10.0),
                              AnimationParameter("zRotation", "Rotation around the Z axis (radians)"),
                              AnimationParameter("xRotation", "Rotation around the X axis (radians)")),
//        center = ParamUsage.USED,
//        delay = ParamUsage.USED,
//        delayDefault = 30,
//        direction = ParamUsage.NOTUSED,
//        distance = ParamUsage.USED,
//        distanceDefault = 1000,
//        spacing = ParamUsage.USED,
//        spacingDefault = 10,
    )
) { leds, params, _ ->
    val color0 = params.colors[0]

    val interMovementDelay = params.intParams.getValue("interMovementDelay").toLong()
    val movementPerIteration = params.doubleParams.getValue("movementPerIteration")
    val zRotation = params.doubleParams.getValue("zRotation")
    val xRotation = params.doubleParams.getValue("xRotation")

    val newManager = PixelLocationManager(
        leds.transformLocations(zRotation, xRotation),
        leds.numLEDs)

    leds.apply {
        val pixelsToModifyPerIteration: MutableMap<Int, MutableList<Int>> = mutableMapOf()
        val changedPixels: MutableList<PixelLocation> = mutableListOf()

        var iteration = 0
        var currentZ = newManager.zMin
        do {
            currentZ += movementPerIteration
            pixelsToModifyPerIteration[iteration] = mutableListOf()
            for (pixel in newManager.pixelLocations) {
                if (pixel !in changedPixels && pixel.location.z <= currentZ) {
                    pixelsToModifyPerIteration[iteration]!!.add(pixel.index)
                    changedPixels.add(pixel)
                }
            }
            iteration++
        } while (currentZ < newManager.zMax)

        for (i in 0 until iteration) {
            for (pixel in pixelsToModifyPerIteration[i]!!)
                leds.setPixelProlongedColor(pixel, color0)
//            for (pixel in pixelsToRipplePerIteration[iteration - 1] ?: pixelsToRipplePerIteration[pixelsToRipplePerIteration.size - 1]!!)
//                leds.revertPixel(pixel)
            delay(interMovementDelay)
        }
    }
}

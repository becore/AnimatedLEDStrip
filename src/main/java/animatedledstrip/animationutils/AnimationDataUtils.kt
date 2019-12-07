/*
 *  Copyright (c) 2019 AnimatedLEDStrip
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

package animatedledstrip.animationutils

import animatedledstrip.colors.ColorContainer
import animatedledstrip.colors.ColorContainerInterface
import animatedledstrip.colors.ColorContainerSerializer
import animatedledstrip.colors.ccpresets.CCBlack
import animatedledstrip.utils.parseHex
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder

class AnimationDataExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(p0: Class<*>?): Boolean {
        return false
    }

    override fun shouldSkipField(field: FieldAttributes?): Boolean {
        return field?.name?.equals("pCols") == true
    }
}

/* JSON Parser */
val gson = GsonBuilder()
    .registerTypeAdapter(ColorContainerInterface::class.java, ColorContainerSerializer())
    .addSerializationExclusionStrategy(AnimationDataExclusionStrategy())
    .create()
    ?: error("Could not create JSON parser")


/* Helper functions for setting values */

/**
 * Sets the `animation` parameter.
 *
 * @param animation The animation to run.
 */
fun AnimationData.animation(animation: Animation): AnimationData {
    this.animation = animation
    return this
}


/**
 * Set the color using a ColorContainer, hex String, or Int or Long
 * in range(0..16777215)
 *
 * @param color
 * @param index The index of the color in the list of colors
 */
fun AnimationData.color(color: Any, index: Int = 0): AnimationData {
    if (colors.size <= index) for (i in colors.size..index) colors += CCBlack

    when (color) {
        is ColorContainerInterface -> colors[index] = color.toColorContainer()
        is Long -> colors[index] = ColorContainer(color)
        is Int -> colors[index] = ColorContainer(color.toLong())
        is String -> colors[index] = ColorContainer(parseHex(color))
    }

    return this
}

/**
 * Append a color to the end of `colors`
 *
 * @param color The color to add
 */
fun AnimationData.addColor(color: ColorContainerInterface): AnimationData {
    colors += color.toColorContainer()
    return this
}

/**
 * Append multiple colors to the end of `colors`
 *
 * @param colors The colors to add
 */
fun AnimationData.addColors(vararg colors: ColorContainerInterface): AnimationData {
    colors.forEach { addColor(it) }
    return this
}

/**
 * Append a color to the end of `colors`
 *
 * @param color A `Long` representing the color to add
 */
fun AnimationData.addColor(color: Long): AnimationData {
    colors += ColorContainer(color)
    return this
}

/**
 * Append multiple colors to the end of `colors`
 *
 * @param colors `Long`s representing the colors to add
 */
fun AnimationData.addColors(vararg colors: Long): AnimationData {
    colors.forEach { addColor(it) }
    return this
}

/**
 * Append a color to the end of `colors`
 *
 * @param color An `Int` representing the color to add
 */
fun AnimationData.addColor(color: Int): AnimationData {
    colors += ColorContainer(color.toLong())
    return this
}

/**
 * Append multiple colors to the end of `colors`
 *
 * @param colors `Int`s representing the colors to add
 */
fun AnimationData.addColors(vararg colors: Int): AnimationData {
    colors.forEach { addColor(it) }
    return this
}

/**
 * Append a color to the end of `colors`
 *
 * @param color A hexadecimal `String` representing the color to add
 */
fun AnimationData.addColor(color: String): AnimationData {
    colors += ColorContainer(parseHex(color))
    return this
}

/**
 * Append multiple colors to the end of `colors`
 *
 * @param colors Hexadecimal `String`s representing the colors to add
 */
fun AnimationData.addColors(vararg colors: String): AnimationData {
    colors.forEach { addColor(it) }
    return this
}


/**
 * Append multiple colors to the end of `colors`
 *
 * @param colors A list of `ColorContainer`s, `Long`s, `Int`s and/or
 * hexadecimal `String`s representing the colors to add
 */
fun AnimationData.addColors(colors: List<*>): AnimationData {
    require(colors.isNotEmpty())
    require(colors.all { it is ColorContainerInterface || it is Long || it is Int || it is String })
    colors.forEach {
        when (it) {
            is ColorContainerInterface -> addColor(it)
            is Long -> addColor(it)
            is Int -> addColor(it)
            is String -> addColor(it)
        }
    }
    return this
}


/* Helpers for setting the first 5 ColorContainers */


/**
 * Set `colors[0]`
 */
fun AnimationData.color0(color: Any) = color(color, 0)

/**
 * Set `colors[1]`
 */
fun AnimationData.color1(color: Any) = color(color, 1)

/**
 * Set `colors[2]`
 */
fun AnimationData.color2(color: Any) = color(color, 2)

/**
 * Set `colors[3]`
 */
fun AnimationData.color3(color: Any) = color(color, 3)

/**
 * Set `colors[4]`
 */
fun AnimationData.color4(color: Any) = color(color, 4)

/**
 * Set the `continuous` parameter.
 *
 * @param continuous A `Boolean`
 */
fun AnimationData.continuous(continuous: Boolean): AnimationData {
    this.continuous = continuous
    return this
}

/**
 * Set the `center` parameter.
 *
 * @param pixel The index of the pixel at the center of a radial animation
 */
fun AnimationData.center(pixel: Int): AnimationData {
    this.center = pixel
    return this
}

/**
 * Set the `delay` parameter.
 *
 * @param delay An `Int` representing the delay time in milliseconds the
 * animation will use
 */
fun AnimationData.delay(delay: Int): AnimationData {
    this.delay = delay.toLong()
    return this
}

/**
 * Set the `delay` parameter.
 *
 * @param delay A `Long` representing the delay time in milliseconds the
 * animation will use
 */
fun AnimationData.delay(delay: Long): AnimationData {
    this.delay = delay
    return this
}

/**
 * Set the `delayMod` parameter.
 *
 * @param delayMod A `Double` that is a multiplier for `delay`
 */
fun AnimationData.delayMod(delayMod: Double): AnimationData {
    this.delayMod = delayMod
    return this
}

/**
 * Set the `direction` parameter.
 *
 * @param direction A `Direction` value ([Direction].`FORWARD` or [Direction].`BACKWARD`)
 */
fun AnimationData.direction(direction: Direction): AnimationData {
    this.direction = direction
    return this
}

/**
 * Set the `direction` parameter with a `Char`.
 *
 * @param direction A `Char` representing `Direction.FORWARD` ('`F`') or
 * `Direction.BACKWARD` ('`B`')
 */
fun AnimationData.direction(direction: Char): AnimationData {
    this.direction = when (direction) {
        'F', 'f' -> Direction.FORWARD
        'B', 'b' -> Direction.BACKWARD
        else -> throw Exception("Direction chars can be 'F' or 'B'")
    }
    return this
}

/**
 * Set the `distance` parameter.
 *
 * @param pixels The number of pixels away from the center pixel
 * that the radial animation should travel
 */
fun AnimationData.distance(pixels: Int): AnimationData {
    this.distance = pixels
    return this
}

/**
 * Set the `endPixel` parameter.
 *
 * @param endPixel An `Int` that is the index of the last pixel showing the
 * animation (inclusive)
 */
fun AnimationData.endPixel(endPixel: Int): AnimationData {
    this.endPixel = endPixel
    return this
}

/**
 * Set the `id` parameter.
 *
 * @param id A `String` used to identify a continuous animation instance
 */
fun AnimationData.id(id: String): AnimationData {
    this.id = id
    return this
}

/**
 * Set the `spacing` parameter.
 *
 * @param spacing An `Int` that is the spacing used by the animation
 */
fun AnimationData.spacing(spacing: Int): AnimationData {
    this.spacing = spacing
    return this
}


/**
 * Simple way to set the speed of an animation. Setting this will modify delayMod accordingly.
 *
 * @param speed The speed to set
 */
fun AnimationData.speed(speed: AnimationSpeed): AnimationData {
    delayMod = when (speed) {
        AnimationSpeed.SLOW -> 0.5
        AnimationSpeed.DEFAULT -> 1.0
        AnimationSpeed.FAST -> 2.0
    }
    return this
}

/**
 * Set the `startPixel` parameter.
 *
 * @param startPixel An `Int` that is the index of the first pixel showing the
 * animation (inclusive)
 */
fun AnimationData.startPixel(startPixel: Int): AnimationData {
    this.startPixel = startPixel
    return this
}

fun Animation.isNonRepetitive() =
    this::class.java.fields[this.ordinal].annotations.find { it is NonRepetitive } is NonRepetitive

fun AnimationData.isContinuous(): Boolean = continuous
    ?: !animation.isNonRepetitive()

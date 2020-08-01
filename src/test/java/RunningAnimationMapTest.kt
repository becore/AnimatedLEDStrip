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

package animatedledstrip.test

import animatedledstrip.animationutils.AnimationData
import animatedledstrip.animationutils.RunningAnimationMap
import animatedledstrip.leds.AnimatedLEDStrip
import kotlinx.coroutines.Job
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RunningAnimationMapTest {

    @Test
    fun testGet() {
        val map = RunningAnimationMap()

        map.map["TEST"] = AnimatedLEDStrip.RunningAnimation(AnimationData(), Job())

        assertTrue { map["TEST"] == map.map["TEST"] }
    }

    @Test
    fun testSet() {
        val map = RunningAnimationMap()
        val testAnim = AnimatedLEDStrip.RunningAnimation(AnimationData(), Job())

        map["TEST"] = testAnim

        assertTrue { map.map["TEST"] == testAnim }
    }

    @Test
    fun testRemove() {
        val map = RunningAnimationMap()

        map.map["TEST"] = AnimatedLEDStrip.RunningAnimation(AnimationData(), Job())

        assertTrue { map.map.containsKey("TEST") }
        map.remove("TEST")
        assertFalse { map.map.containsKey("TEST") }
    }

    @Test
    fun testEntries() {
        val map = RunningAnimationMap()
        val testAnim = AnimatedLEDStrip.RunningAnimation(AnimationData(), Job())
        map.map["TEST"] = testAnim

        assertTrue { map.entries.contains(Pair("TEST", testAnim)) }
        assertTrue { map.entries.size == 1 }
    }

    @Test
    fun testIDs() {
        val map = RunningAnimationMap()
        map.map["TEST"] = AnimatedLEDStrip.RunningAnimation(AnimationData(), Job())

        assertTrue { map.ids.contains("TEST") }
        assertTrue { map.ids.size == 1 }
    }

    @Test
    fun testAnimations() {
        val map = RunningAnimationMap()
        val testAnim = AnimatedLEDStrip.RunningAnimation(AnimationData(), Job())
        map.map["TEST"] = testAnim

        assertTrue { map.animations.contains(testAnim) }
        assertTrue { map.animations.size == 1 }
    }
}
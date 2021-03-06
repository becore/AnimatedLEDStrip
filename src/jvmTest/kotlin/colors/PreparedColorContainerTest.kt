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

package animatedledstrip.test.colors

import animatedledstrip.colors.ColorContainer
import animatedledstrip.colors.PreparedColorContainer
import animatedledstrip.colors.shift
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import kotlin.test.assertTrue

class PreparedColorContainerTest : StringSpec(
    {
        "get" {
            checkAll(Arb.int(0..0xFFFFFF),
                     Arb.int(0..0xFFFFFF),
                     Arb.int(0..0xFFFFFF),
                     Arb.int(0..0xFFFFFF)) { c1, c2, c3, c4 ->
                val testCC = PreparedColorContainer(listOf(c1, c2, c3, c4), listOf(c1, c2, c3, c4))

                testCC[0] shouldBe c1
                testCC[1] shouldBe c2
                testCC[2] shouldBe c3
                testCC[3] shouldBe c4
                testCC[5] shouldBe 0
                testCC.color shouldBe 0
            }
        }

        "equality" {
            checkAll(25, Arb.list(Arb.int(0, 0xFFFFFF), 1..500)) { c ->
                checkAll(10, Arb.int(1..500)) { len ->
                    val testPCC = ColorContainer(c.toMutableList()).prepare(len)

                    checkAll(5, Arb.int(1..500).filter { it != len }) { len2 ->
                        testPCC shouldBe ColorContainer(c.toMutableList()).prepare(len2)
                        testPCC.hashCode() shouldNotBe ColorContainer(c.toMutableList()).prepare(len2).hashCode()
                    }
                }
            }
        }

        "string" {
            ColorContainer(0xFF3B82).prepare(1).toString() shouldBe "[ff3b82]"

            ColorContainer(0xFF7B50, 0xFFFFFF).prepare(2).toString() shouldBe "[ff7b50, ffffff]"
            ColorContainer().prepare(1).toString() shouldBe "[0]"
        }

        "contains" {
            checkAll(Arb.list(Arb.int(0..0xFFFFFF), 1..1500), Arb.int(1..1500)) { c, n ->
                val testPCC = ColorContainer(c.toMutableList()).prepare(n)

                for (i in c) {
                    if (n >= c.size) (i in testPCC).shouldBeTrue()
                }

                for (i in Arb.list(Arb.int().filter { it !in c }, 10..10).next())
                    (i in testPCC).shouldBeFalse()
            }
        }

        "prepare" {
            checkAll(Arb.list(Arb.int(0..0xFFFFFF), 1..1500), Arb.int(1..1500)) { c, n ->
                val testPCC = ColorContainer(c.toMutableList()).prepare(n)

                testPCC.prepare(n).colors.shouldContainExactly(testPCC.colors)
                testPCC.prepare(n) shouldBeSameInstanceAs testPCC

                testPCC.prepare(n+1).colors shouldNotBe testPCC.colors
                testPCC.prepare(n+1) shouldNotBeSameInstanceAs testPCC
            }
        }

        "size" {
            checkAll(Arb.list(Arb.int(0..0xFFFFFF), 1..1500), Arb.int(1..1500)) { c, n ->
                ColorContainer(c.toMutableList()).prepare(n).size shouldBe n
            }
        }

        "toColorContainer" {
            checkAll(Arb.list(Arb.int(0..0xFFFFFF), 1..1500), Arb.int(1..1500)) { c, n ->
                val testPCC = ColorContainer(c.toMutableList()).prepare(n)

                val testCC = testPCC.toColorContainer()

                testCC.colors.shouldContainExactly(testPCC.colors)
                if (n != c.size) testCC shouldNotBe testPCC
                else testCC shouldBe testPCC
            }
        }

        "original ColorContainer" {
            checkAll(Arb.list(Arb.int(0..0xFFFFFF), 1..500), Arb.int(1..500)) { c, n ->
                val testPCC = ColorContainer(c.toMutableList()).prepare(n)

                val testCC = testPCC.originalColorContainer()

                testCC.colors.shouldContainExactly(c)
                testCC shouldBe testPCC
            }
        }

        "offset by".config(enabled = false) {
            val c = ColorContainer(
                0x0000FF, 0x00FFFF, 0xFF00FF,
                0x00FF00, 0xFF0000, 0xFFFFFF,
            ).prepare(6)

            assertTrue {
                c.colors ==
                        listOf<Long>(
                            0x0000FF, 0x00FFFF, 0xFF00FF,
                            0x00FF00, 0xFF0000, 0xFFFFFF,
                        )
            }
            assertTrue {
                c.shift(0).colors ==
                        listOf<Long>(
                            0x0000FF, 0x00FFFF, 0xFF00FF,
                            0x00FF00, 0xFF0000, 0xFFFFFF,
                        )
            }
            assertTrue {
                c.shift(4).colors ==
                        listOf<Long>(
                            0xFF00FF, 0x00FF00, 0xFF0000,
                            0xFFFFFF, 0x0000FF, 0x00FFFF,
                        )
            }
            assertTrue {
                c.shift(-4).colors ==
                        listOf<Long>(
                            0xFF0000, 0xFFFFFF, 0x0000FF,
                            0x00FFFF, 0xFF00FF, 0x00FF00,
                        )
            }
            assertTrue {
                c.shift(10).colors ==
                        c.shift(4).colors
            }
            assertTrue {
                c.shift(-10).colors ==
                        c.shift(-4).colors
            }
        }
    }
)

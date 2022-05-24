package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun parseBlock2() {
    // Blocks are aggregated in a tree like fashion. A block can contain inner blocks.
    // The inner2 block is right after the text to avoid creating an empty line at the end of inner1.
    // The root block however has a return line. It is transcribed in the result.
    val block = """
        This is {{ something }}.
        --{ inner1
            an amazing {{ object }}--{ inner2
                fish named {{ name }}
            }--
        }--
    """
    val container = TT.block(block)
    // Get the corresponding block with the get(vararg) method.
    val fishNamedDory = container["inner1", "inner2"] bindTo TT.group("name" to TT.value("Dory"))
    val anAmazing = container["inner1"] bindTo TT.group("object" to fishNamedDory)
    // Or just get the root template from the get() method.
    val thisIs = container.get() bindTo TT.group("something" to anAmazing)
    assertEqual("This is an amazing fish named Dory.\n", thisIs.renderToString())
}
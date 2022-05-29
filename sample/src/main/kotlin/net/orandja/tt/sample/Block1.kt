package net.orandja.tt.sample

import net.orandja.tt.*
import net.orandja.tt.block.Block

fun block1() {
    // Blocks are just parts of texts inside a string.
    // Like tags inside templates, blocks are defined between delimiters.
    // Defaults delimiters are "--{" and "}--".
    // The name of a block is the first word found between the block delimiters.
    // It can be anything, but it should not contain space.
    val block = """
        The root is here !
        --{ block1
            What an awesome block
            --{ inner-block1
                Wow, a block inside a block
                
            }--
        }--
        --{ block2 Sure blocks are awesome }--
    """

    // You can parse blocks with TT.block
    val container: Block = TT.block(block)

    // To get the content of a block simply call the get["key"] method.
    // The second method is inside parseBlock3
    assertEqual("What an awesome block", container["block1"])
    assertEqual("Sure blocks are awesome ", container["block2"]) // Blocks do not trim the end on single line.

    // For inner block you can chain keys on the get[vararg] method
    assertEqual("Wow, a block inside a block\n", container["block1", "inner-block1"])

    // To get the root block, just call the get without arguments
    assertEqual("The root is here !", container.get())
}

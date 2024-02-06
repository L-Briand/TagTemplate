package net.orandja.tt

import net.orandja.tt.parser.CharStream

/** Implementation of [CharStream] for any kind of [CharSequence] */
internal class StringCharStream(val content: CharSequence) : CharStream {
    private var index = 0
    override fun read(): Char = if (index < content.length) {
        val result = content[index]
        index += 1
        result
    } else Char.MAX_VALUE
}
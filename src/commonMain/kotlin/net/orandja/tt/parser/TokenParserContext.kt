package net.orandja.tt.parser

/**
 * Class representing the context for token parsing.
 *
 * @see TokenParser
 */
internal class TokenParserContext(
    val reader: CharStream,
    defaultStartDelimiter: CharSequence,
    defaultStopDelimiter: CharSequence,
) {
    /** Current delimiter to find. true: '{{', false '}}' */
    var searchingDelimStart: Boolean = true

    /** The number of characters matching between the delimiter and current reading. */
    var delimMatchIdx: Int = 0

    /** Start tag delimiter currently used while parsing the document */
    var startDelim: CharSequence = defaultStartDelimiter

    /** Stop tag delimiter currently used while parsing the document */
    var stopDelim: CharSequence = defaultStopDelimiter

    // Current char reader

    var peeked: Char = Char.MIN_VALUE

    @Suppress("NOTHING_TO_INLINE")
    inline fun next(): Char {
        val peeked = peek()
        consume()
        return peeked
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun peek(): Char {
        if (peeked != Char.MIN_VALUE) return peeked
        peeked = reader.read()
        return peeked
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun consume() {
        if (peeked == Char.MIN_VALUE) return
        readBuffer.append(peeked)
        peeked = Char.MIN_VALUE
    }

    var readBuffer = StringBuilder(128)

    @Suppress("NOTHING_TO_INLINE")
    inline fun getBuffer(trimEndBy: Int): CharSequence {
        val result = readBuffer.substring(0, (readBuffer.length - trimEndBy))
        readBuffer.clear()
        return result
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun drop(count: Int = 1) = readBuffer.setLength(readBuffer.length - count)
}
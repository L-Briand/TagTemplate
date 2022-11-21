package net.orandja.tt.utils

import net.orandja.tt.templates.Delimiters

object TemplateUtils {

    @JvmStatic
    @Suppress("ReplaceManualRangeWithIndicesCalls")
    fun getTokens(raw: CharSequence, delimiters: Delimiters): List<Token> {
        val tokens = ArrayList<Token>(32)
        val words = arrayOf(delimiters.start, delimiters.end)
        val wordToken = intArrayOf(Token.DELIMITER_START, Token.DELIMITER_END)
        tokens.add(Token.start)
        var ri = 0
        while (ri < raw.length) {
            for (wi in 0 until words.size) {
                if (raw[ri] == words[wi][0] && raw.regionMatches(ri, words[wi], 0, words[wi].length)) {
                    tokens.add(Token(wordToken[wi], ri))
                    ri += words[wi].length - 1
                }
            }
            ri++
        }
        tokens.add(Token(Token.END, raw.length))
        return tokens
    }

    @JvmStatic
    fun getPartsFromTokens(tokens: List<Token>, raw: CharSequence, delimiters: Delimiters): Array<TemplatePart> =
        Array(tokens.size - 1) { i ->
            val current = tokens[1 + i]
            val previous = tokens[i]
            when (current.type) {
                Token.END, Token.DELIMITER_START -> when (previous.type) {
                    Token.START -> TemplatePart.Chunk(
                        previous.at,
                        current.at - previous.at
                    )

                    Token.DELIMITER_END -> TemplatePart.Chunk(
                        previous.at + delimiters.end.length,
                        current.at - previous.at - delimiters.end.length
                    )

                    else -> throw IllegalStateException()
                }

                Token.DELIMITER_END -> when (previous.type) {
                    Token.DELIMITER_START -> {
                        val string = raw.subSequence(previous.at + delimiters.start.length, current.at)
                        TemplatePart.Tag(string.trim().toString())
                    }

                    else -> throw IllegalStateException()
                }

                else -> throw IllegalStateException()
            }
        }.filter { !(it is TemplatePart.Chunk && it.len == 0) }.toTypedArray()
}

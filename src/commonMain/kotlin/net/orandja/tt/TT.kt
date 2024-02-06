package net.orandja.tt

import net.orandja.tt.escape.Escaper
import net.orandja.tt.escape.UnEscape
import net.orandja.tt.parser.CharStream
import net.orandja.tt.parser.Token
import net.orandja.tt.parser.TokenParser
import net.orandja.tt.parser.TokenParserContext

open class TT(
    private val escaper: Escaper = UnEscape,
    private val start: CharSequence = "{{",
    private val stop: CharSequence = "}}",
) {

    companion object : TT()

    operator fun invoke(input: CharSequence, vararg arguments: Pair<String, *>) =
        invoke(input, arguments.toMap())

    operator fun invoke(input: CharSequence, arguments: Map<String, *>) =
        StringBuilder(input.length).apply { invoke(input, arguments) { append(it) } }.toString()

    operator fun invoke(input: CharSequence, vararg arguments: Pair<String, *>, writer: (CharSequence) -> Unit) =
        invoke(StringCharStream(input), arguments.toMap(), writer)

    operator fun invoke(input: CharSequence, arguments: Map<String, *>, writer: (CharSequence) -> Unit) =
        invoke(StringCharStream(input), arguments, writer)

    operator fun invoke(input: CharStream, arguments: Map<String, *>, writer: (CharSequence) -> Unit) {
        val tokens = TokenParser.parse(TokenParserContext(input, start, stop))
        for (token in tokens) {
            when (token.type) {
                Token.TAG -> {
                    escaper.escape(arguments[token.content].toString(), writer)
                }

                Token.STATIC_CONTENT -> {
                    writer(token.content)
                }
            }
        }
    }
}

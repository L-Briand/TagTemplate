@file:Suppress("ReplaceSizeCheckWithIsNotEmpty")

package net.orandja.tt.parser

object TokenParser {

    /**
     * Parses a token sequence from the given [context].
     *
     * @param context The token parsing context.
     * @return A sequence of [Token] objects.
     */
    internal fun parse(context: TokenParserContext): Sequence<Token> = sequence { parseTokens(context) }

    /**
     * Parses tokens from the given sequence and yields them using the provided SequenceScope.
     *
     * @param ctx The context used for token parsing.
     */
    private suspend fun SequenceScope<Token>.parseTokens(ctx: TokenParserContext) {
        while (true) {
            val current = ctx.next()
            if (current == Char.MAX_VALUE) break
            if (ctx.searchingDelimStart) {
                // Searching for startDelim in the document (i.e. '{{')
                if (current != ctx.startDelim[ctx.delimMatchIdx]) {
                    ctx.delimMatchIdx = 0
                    continue
                }
                ctx.delimMatchIdx++
                if (ctx.delimMatchIdx == ctx.startDelim.length) {
                    ctx.delimMatchIdx = 0
                    val content = ctx.getBuffer(trimEndBy = ctx.startDelim.length)
                    if (content.length != 0) yield(Token(Token.STATIC_CONTENT, content))
                    ctx.searchingDelimStart = false
                }
            } else {
                // Searching for stopDelim in the document (i.e. '}}')
                if (current != ctx.stopDelim[ctx.delimMatchIdx]) {
                    ctx.delimMatchIdx = 0
                    continue
                }
                ctx.delimMatchIdx++
                if (ctx.delimMatchIdx == ctx.stopDelim.length) {
                    ctx.delimMatchIdx = 0
                    val content = ctx.getBuffer(trimEndBy = ctx.stopDelim.length)
                    yield(Token(Token.TAG, content.trim()))
                    ctx.searchingDelimStart = true
                }
            }
        }
        ctx.drop()
        val content = ctx.getBuffer(trimEndBy = 0)
        if (content.length != 0) yield(Token(Token.STATIC_CONTENT, content))
    }
}
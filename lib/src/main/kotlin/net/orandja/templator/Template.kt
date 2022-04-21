package net.orandja.templator

import net.orandja.templator.Template.Range.Type.TAG
import net.orandja.templator.Template.Range.Type.TEXT


class Template(
    private val raw: CharSequence,
    delimiters: Delimiters = Delimiters(),
) : TemplateRenderer {

    private data class Range(val type: Type, val from: Int, val to: Int) {
        enum class Type { TEXT, TAG }
    }

    private val ranges: List<Range>

    init {
        val updatableRanges = mutableListOf<Range>()
        val (s, e) = delimiters

        var idx = 0
        var lastIdx = 0
        while (idx < raw.length) {
            // Might be more optimised
            if (raw[idx] == s[0] && raw.regionMatches(idx, s, 0, s.length)) {
                if (updatableRanges.size == 0 || updatableRanges.lastOrNull()?.type == TAG) {
                    updatableRanges += Range(TEXT, lastIdx, idx)
                    lastIdx = idx
                    idx += s.length - 1
                }
            }
            if (raw[idx] == e[0] && raw.regionMatches(idx, e, 0, e.length)) {
                if (updatableRanges.lastOrNull()?.type == TEXT) {
                    updatableRanges += Range(TAG, lastIdx + s.length, idx)
                    idx += e.length
                    lastIdx = idx
                }
            }
            idx++
        }
        if (lastIdx < raw.length - 1) {
            updatableRanges += Range(TEXT, lastIdx, raw.length)
        }
        ranges = updatableRanges.toList()
    }

    override suspend fun render(
        key: String?,
        context: TemplateRenderer,
        onNew: (CharSequence) -> Unit
    ): Boolean {
        for (range in ranges) {
            when (range.type) {
                TEXT -> onNew(raw.subSequence(range.from, range.to))
                TAG -> {
                    val tag = raw.subSequence(range.from, range.to)
                    context.render(tag.trim().toString(), context, onNew)
                }
            }
        }
        return true
    }
}


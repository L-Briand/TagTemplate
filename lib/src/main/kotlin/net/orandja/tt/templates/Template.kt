package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer
import net.orandja.tt.templates.Range.Type.TAG
import net.orandja.tt.templates.Range.Type.TEXT

class Template private constructor(
    private val raw: CharSequence,
    private val ranges: List<Range>,
) : TemplateRenderer() {
    companion object {
        @JvmStatic
        operator fun invoke(raw: CharSequence, delimiters: Delimiters): Template {
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
            if (lastIdx < raw.length) {
                updatableRanges += Range(TEXT, lastIdx, raw.length)
            }
            return Template(raw, updatableRanges.toList())
        }
    }

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean {
        val ctxs = mergeContexts(contexts)
        // search in context to render the specified key
        if (key != null) return ctxs.firstOrNull { it.validateTag(key) }?.render(key, ctxs, onNew) ?: false
        for (range in ranges) {
            when (range.type) {
                TEXT -> onNew(raw.subSequence(range.from, range.to))
                TAG -> {
                    val tag = raw.subSequence(range.from, range.to).trim().toString()
                    val renderer = ctxs.firstOrNull { it.validateTag(tag) }
                        ?: throw IllegalStateException("tag {{ $tag }} not found")
                    renderer.render(tag, ctxs, onNew)
                }
            }
        }
        return true
    }

    override fun duplicate(): TemplateRenderer = Template(raw, ranges)
    override suspend fun validateTag(key: String): Boolean = false
    override fun toString(): String = """'$raw'$contextString"""
}

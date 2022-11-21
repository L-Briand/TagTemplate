package net.orandja.tt.utils

import net.orandja.tt.templates.Delimiters
import net.orandja.tt.utils.TemplateUtils.getPartsFromTokens
import net.orandja.tt.utils.TemplateUtils.getTokens

interface SourceTemplate {
    val templateSource: CharSequence
    val parts: Array<TemplatePart>

    class Static(
        override val templateSource: CharSequence,
        delimiters: Delimiters,
    ) : SourceTemplate {
        override val parts = getPartsFromTokens(getTokens(templateSource, delimiters), templateSource, delimiters)
    }

    class Delegate(
        _source: () -> SourceTemplate
    ) : SourceTemplate {
        override val templateSource: CharSequence = _source().templateSource
        override val parts: Array<TemplatePart> = _source().parts
    }

    class CachedStream(
        private val _source: SourceStream,
        private val delimiters: Delimiters,
    ) : SourceTemplate {
        private lateinit var cacheSource: CharSequence
        private lateinit var cacheParts: Array<TemplatePart>
        private var lastUpdate: Long? = null

        private fun updateCache() {
            cacheSource = String(_source.inputStream.readAllBytes(), Charsets.UTF_8)
            cacheParts = getPartsFromTokens(getTokens(cacheSource, delimiters), cacheSource, delimiters)
            lastUpdate = _source.lastUpdate
        }

        override val templateSource: CharSequence
            get() {
                if (_source.lastUpdate != lastUpdate) updateCache()
                return cacheSource
            }

        override val parts: Array<TemplatePart>
            get() {
                if (_source.lastUpdate != lastUpdate) updateCache()
                return cacheParts
            }
    }
}

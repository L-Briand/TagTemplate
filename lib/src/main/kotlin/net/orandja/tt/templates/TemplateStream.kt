package net.orandja.tt.templates

import net.orandja.tt.StreamProvider
import net.orandja.tt.TemplateRenderer
import net.orandja.tt.templates.Range.Type.TAG
import net.orandja.tt.templates.Range.Type.TEXT

class TemplateStream(
    private val streamProvider: StreamProvider,
    private val delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE,
    private var lastModified: Long = Long.MIN_VALUE,
    private var ranges: List<Range> = listOf(),
) : TemplateRenderer() {
    private val start = delimiters.start.toString().toByteArray()
    private val stop = delimiters.end.toString().toByteArray()

    private fun refreshRange() {
        val lastUpdated = streamProvider.lastUpdate()
        if (ranges.isNotEmpty() && lastUpdated <= lastModified) return
        lastModified = lastUpdated

        var idx = 0
        var lastIdx = 0
        var startCounter = 0
        var stopCounter = 0

        val updatableRanges = mutableListOf<Range>()
        streamProvider.source().use {
            while (true) {
                val byte = it.read().toByte()
                if (byte == (-1).toByte()) break
                // last means idx at 1 for "{{" and end means idx at 2
                if (byte == start[startCounter]) {
                    startCounter += 1
                    if (startCounter == start.size && (updatableRanges.size == 0 || updatableRanges.lastOrNull()?.type == TAG)) {
                        // idx == last of start
                        // first of start
                        val endPosition = idx - start.size + 1
                        // from end of stop to first of start
                        updatableRanges += Range(TEXT, lastIdx, endPosition)
                        lastIdx = endPosition // first of start
                        startCounter = 0
                    }
                } else startCounter = 0

                if (byte == stop[stopCounter]) {
                    stopCounter += 1
                    if (stopCounter == stop.size && (updatableRanges.lastOrNull()?.type == TEXT)) {
                        // idx == last of stop
                        // first of stop
                        val endPosition = idx - start.size + 1
                        // from end of start to first of stop
                        updatableRanges += Range(TAG, lastIdx + start.size, endPosition)
                        lastIdx = idx + 1 // end of stop
                        stopCounter = 0
                    }
                } else stopCounter = 0

                idx++
            }
        }
        if (lastIdx < idx) {
            updatableRanges += Range(TEXT, lastIdx, idx)
        }
        ranges = updatableRanges
    }

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean {
        val ctxs = mergeContexts(contexts)
        // search in context to render the specified key
        if (key != null) return ctxs.firstOrNull { it.validateTag(key) }?.render(key, ctxs, onNew) ?: false
        refreshRange()
        streamProvider.source().use {
            for (rangeIdx in ranges.indices) {
                when (ranges[rangeIdx].type) {
                    TEXT -> {
                        val bytes = it.readNBytes(ranges[rangeIdx].to - ranges[rangeIdx].from)
                        onNew(String(bytes))
                        if (rangeIdx != ranges.lastIndex) it.skipNBytes(start.size.toLong())
                    }
                    TAG -> {
                        val bytes = it.readNBytes(ranges[rangeIdx].to - ranges[rangeIdx].from)
                        val tag = String(bytes).trim()
                        val renderer = ctxs.firstOrNull { it.validateTag(tag) }
                            ?: throw IllegalStateException("tag {{ $tag }} not found")
                        renderer.render(tag, ctxs, onNew)
                        it.skipNBytes(stop.size.toLong())
                    }
                }
            }
        }
        return true
    }

    override fun duplicate(): TemplateRenderer =
        TemplateStream(streamProvider, delimiters, lastModified, ranges)

    override suspend fun validateTag(key: String): Boolean = false

    override fun toString(): String =
        """'${streamProvider.source().use { it.readAllBytes() }.toString(Charsets.UTF_8)}'$contextString"""
}

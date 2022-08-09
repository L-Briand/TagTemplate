package net.orandja.tt

import net.orandja.tt.templates.*
import java.io.InputStream
import java.nio.charset.Charset

class TT {
    companion object {

        @JvmStatic
        fun template(template: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE) =
            Template(template, delimiters)

        @JvmStatic
        fun templateStream(
            lastUpdate: () -> Long,
            source: () -> InputStream,
            delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE,
        ) = templateStream(StreamProvider(lastUpdate, source), delimiters)

        @JvmStatic
        fun templateStream(
            streamProvider: StreamProvider,
            delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE,
        ) = TemplateStream(streamProvider, delimiters)

        @JvmStatic
        fun value(value: CharSequence) = Value { value }

        @JvmStatic
        fun valueDelegate(source: () -> CharSequence) = Value(source)

        @JvmStatic
        fun valueStream(charset: Charset = Charsets.UTF_8, source: () -> InputStream) =
            Value { String(source().use { it.readAllBytes() }, charset) }

        @JvmStatic
        fun values(vararg values: Pair<String, String>) =
            templates(values.associate { it.first to value(it.second) })

        @JvmStatic
        fun values(values: Map<String, String>) = templates(values.mapValues { value(it.value) })

        @JvmStatic
        fun templates(vararg renders: Pair<String, TemplateRenderer>) = templates(renders.toMap())

        @JvmStatic
        fun templates(renders: Map<String, TemplateRenderer>) = Group(renders)

        @JvmStatic
        fun templates(provider: TemplateProvider) = Group(provider)

        @JvmStatic
        fun repeat(times: Int, render: TemplateRenderer) = Repeater(times, render)

        @JvmStatic
        fun roll(elements: Iterable<TemplateRenderer>) =
            roll(*((elements as? List<TemplateRenderer>) ?: elements.toList()).toTypedArray())

        @JvmStatic
        fun roll(vararg render: TemplateRenderer) = Roller(*render)

        @JvmStatic
        fun list(on: TemplateRenderer, elements: Iterable<TemplateRenderer>) =
            list(on, *((elements as? List<TemplateRenderer>) ?: elements.toList()).toTypedArray())

        @JvmStatic
        fun list(on: TemplateRenderer, vararg elements: TemplateRenderer) =
            repeat(elements.size, on) bindTo roll(*elements)

        @JvmStatic
        fun group(
            vararg templates: Pair<String, TemplateRenderer>,
            separator: String = Delimiters.DEFAULT_GROUP_SEPARATOR
        ): TemplateRenderer {
            val new = templates.fold(mutableMapOf<String, TemplateRenderer>()) { acc, pair ->
                val template = pair.second
                if (template is Group)
                    template.provider.keys().forEach {
                        acc["${pair.first}$separator$it"] = template[it]!!
                    }
                else acc["${pair.first}$separator"] = template
                acc
            }
            return templates(new)
        }

        @JvmStatic
        fun merge(
            vararg templates: TemplateRenderer,
        ): TemplateRenderer {
            val new = templates.fold(mutableMapOf<String, TemplateRenderer>()) { acc, template ->
                if (template is Group)
                    template.provider.keys().forEach {
                        acc[it] = template[it]!!
                    }
                else throw IllegalStateException("Merge should only contains groups")
                acc
            }
            return templates(new)
        }
    }
}

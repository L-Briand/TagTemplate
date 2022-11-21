package net.orandja.tt

import net.orandja.tt.templates.*
import net.orandja.tt.utils.SourceStream
import net.orandja.tt.utils.SourceTemplate
import net.orandja.tt.utils.SourceValue
import java.io.InputStream
import java.nio.charset.Charset

class TT {
    companion object {

        @JvmStatic
        fun template(template: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE) =
            template(SourceTemplate.Static(template, delimiters))

        @JvmStatic
        fun template(source: SourceTemplate) = Template(source)

        @JvmStatic
        fun templateDelegate(delegate: () -> SourceTemplate) = template(SourceTemplate.Delegate(delegate))

        @JvmStatic
        fun templateStream(
            sourceStream: SourceStream,
            delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE,
        ) = template(SourceTemplate.CachedStream(sourceStream, delimiters))

        @JvmStatic
        fun value(value: CharSequence) = Value(SourceValue.Static(value))

        @JvmStatic
        fun value(source: SourceValue) = Value(source)

        @JvmStatic
        fun valueDelegate(delegate: () -> CharSequence) = Value(SourceValue.Delegate(delegate))

        @JvmStatic
        fun valueStream(charset: Charset = Charsets.UTF_8, source: () -> InputStream) =
            valueDelegate { String(source().use { it.readAllBytes() }, charset) }

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

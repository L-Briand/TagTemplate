package net.orandja.tt

import net.orandja.tt.block.BlockParser
import net.orandja.tt.templates.*
import java.io.InputStream

class TT {
    companion object {
        @JvmStatic
        fun template(template: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE) =
            Template(template, delimiters)

        @JvmStatic
        fun templateStream(
            sourceLastUpdate: () -> Long,
            source: () -> InputStream,
            delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE,
        ) = TemplateStream(sourceLastUpdate, source, delimiters)

        @JvmStatic
        fun value(value: CharSequence) = Value(value)

        @JvmStatic
        fun valueStream(source: () -> InputStream) = ValueStream(source)

        @JvmStatic
        fun values(vararg values: Pair<String, String>) =
            templates(values.associate { it.first to value(it.second) })

        @JvmStatic
        fun templates(vararg renders: Pair<String, TemplateRenderer>) = templates(renders.toMap())

        @JvmStatic
        fun templates(renders: Map<String, TemplateRenderer>) = Group(renders)

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
        fun block(block: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT_BLOCK) =
            BlockParser.parseBlock(block, delimiters)

        @JvmStatic
        fun templatesFromBlock(
            block: CharSequence,
            separator: String = Delimiters.DEFAULT_SEPARATOR,
            blockDelimiter: Delimiters = Delimiters.DEFAULT_BLOCK,
            templateDelimiter: Delimiters = Delimiters.DEFAULT_TEMPLATE
        ): TemplateRenderer = templates(block(block, blockDelimiter).asTemplateMap(separator, templateDelimiter))

        @JvmStatic
        fun merge(
            vararg templates: Pair<String, TemplateRenderer>,
            separator: String = Delimiters.DEFAULT_MERGE_SEPARATOR
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
    }
}

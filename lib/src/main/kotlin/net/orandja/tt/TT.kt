package net.orandja.tt

import net.orandja.tt.block.BlockParser
import net.orandja.tt.templates.Delimiters
import net.orandja.tt.templates.Group
import net.orandja.tt.templates.Repeater
import net.orandja.tt.templates.Roller
import net.orandja.tt.templates.Template
import net.orandja.tt.templates.Value

class TT {
    companion object {
        @JvmStatic
        fun template(template: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE) = Template(template, delimiters)

        @JvmStatic
        fun value(value: CharSequence) = Value(value)

        @JvmStatic
        fun group(vararg renders: Pair<String, TemplateRenderer>) = group(renders.toMap())

        @JvmStatic
        fun group(renders: Map<String, TemplateRenderer>) = Group(renders)

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
        fun list(on: TemplateRenderer, vararg elements: TemplateRenderer) = repeat(elements.size, on) bindTo roll(*elements)

        @JvmStatic
        fun block(block: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT_BLOCK) = BlockParser.parseBlock(block, delimiters)
    }
}

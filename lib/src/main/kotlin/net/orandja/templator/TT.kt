package net.orandja.templator

import net.orandja.templator.tt.ContextSwitcher
import net.orandja.templator.tt.Group
import net.orandja.templator.tt.Repeater
import net.orandja.templator.tt.Roller
import net.orandja.templator.tt.Template
import net.orandja.templator.tt.Value

object TT {
    @JvmStatic
    fun template(str: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT) = Template(str, delimiters)

    @JvmStatic
    fun value(value: CharSequence) = Value(value)

    @JvmStatic
    fun repeat(times: Int, render: TemplateRenderer) = Repeater(times, render)

    @JvmStatic
    fun group(vararg renders: Pair<String, TemplateRenderer>) = group(renders.toMap())

    @JvmStatic
    fun group(renders: Map<String, TemplateRenderer>) = Group(renders)

    @JvmStatic
    fun roll(elements: Iterable<TemplateRenderer>) = roll(*elements.toList().toTypedArray())

    @JvmStatic
    fun roll(vararg render: TemplateRenderer) = Roller(*render)

    @JvmStatic
    fun onContext(render: TemplateRenderer, context: TemplateRenderer) = ContextSwitcher(render, context)
}

package net.orandja.tt

import net.orandja.tt.templates.Delimiters
import net.orandja.tt.templates.Group
import net.orandja.tt.templates.Repeater
import net.orandja.tt.templates.Roller
import net.orandja.tt.templates.Template
import net.orandja.tt.templates.Value

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
}

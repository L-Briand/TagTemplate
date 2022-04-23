package net.orandja.templator.tt

import net.orandja.templator.TemplateRenderer

class Repeater(
    private val times: Int,
    private val render: TemplateRenderer
) : TemplateRenderer {
    override fun toString(): String = "repeat($times) { $render }"
    override suspend fun render(key: String?, context: TemplateRenderer, onNew: (CharSequence) -> Unit): Boolean =
        (0 until times).all { render.render(key, context, onNew) }

    override fun get(vararg keys: String?): TemplateRenderer =
        if(keys.isEmpty()) this else render.get(*keys.sliceArray(1 until keys.size))
}
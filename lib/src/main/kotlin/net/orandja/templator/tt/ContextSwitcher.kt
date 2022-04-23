package net.orandja.templator.tt

import net.orandja.templator.TemplateRenderer

class ContextSwitcher(
    private val render: TemplateRenderer,
    private val context: TemplateRenderer,
) : TemplateRenderer {
    override fun toString(): String =
        "context { ctx:$context, with:$render }"

    override suspend fun render(key: String?, context: TemplateRenderer, onNew: (CharSequence) -> Unit): Boolean =
        render.render(key, this.context, onNew)

    override fun get(vararg keys: String?): TemplateRenderer =
        if (keys.isEmpty()) this else render.get(*keys.sliceArray(1 until keys.size))

    override fun context(): TemplateRenderer = context
}
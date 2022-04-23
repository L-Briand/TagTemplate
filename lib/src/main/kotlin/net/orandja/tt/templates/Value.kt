package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer

class Value(
    private val value: CharSequence
) : TemplateRenderer {
    override fun toString(): String = "V'$value'"
    override suspend fun render(key: String?, context: TemplateRenderer, onNew: (CharSequence) -> Unit): Boolean {
        onNew(value)
        return true
    }

    override fun get(vararg keys: String?): TemplateRenderer = this
}

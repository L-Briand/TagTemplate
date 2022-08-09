package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer

class Value(
    private val source: () -> CharSequence
) : TemplateRenderer() {

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean {
        onNew(source())
        return true
    }

    override fun duplicate(): TemplateRenderer = this
    override suspend fun validateTag(key: String): Boolean = false
    override var context: TemplateRenderer?
        get() = null
        set(value) {
            throw IllegalStateException("Value template's context should not be set")
        }

    override fun toString(): String = """'${source()}'"""
}

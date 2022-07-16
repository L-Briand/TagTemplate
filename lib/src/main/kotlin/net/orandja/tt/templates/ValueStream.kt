package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer
import java.io.InputStream

class ValueStream(
    private val source: () -> InputStream,
) : TemplateRenderer() {

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean {
        onNew(String(source().use { it.readAllBytes() }))
        return true
    }

    override fun duplicate(): TemplateRenderer = this

    override suspend fun validateTag(key: String): Boolean = false

    override var context: TemplateRenderer?
        get() = null
        set(value) { throw IllegalStateException("Value template's context should not be set") }

    override fun toString(): String = "V'${String(source().readAllBytes())}'"
}

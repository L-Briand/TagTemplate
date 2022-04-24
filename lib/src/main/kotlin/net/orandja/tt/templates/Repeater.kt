package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer

class Repeater(
    private val times: Int,
    private val backing: TemplateRenderer
) : TemplateRenderer() {
    override fun toString(): String = "repeat($times) { $backing }"

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean = (0 until times).all { backing.render(key, mergeContexts(contexts), onNew) }

    override fun clone(): TemplateRenderer = Repeater(times, backing.clone())
    override suspend fun validateTag(key: String): Boolean = backing.validateTag(key)
    override fun get(vararg keys: String?): TemplateRenderer? = backing.get(*keys)
}

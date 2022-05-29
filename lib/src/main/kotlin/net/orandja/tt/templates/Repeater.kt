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
    ): Boolean = (0 until times).all {
        backing.render(key, mergeContexts(contexts), onNew)
            .also { (context as? Roller)?.increment() }
    }

    override var context: TemplateRenderer? = null
        set(value) {
            if (value is Roller) value.autoRoll = false
            field = value
        }

    override fun duplicate(): TemplateRenderer = Repeater(times, backing.duplicate())
    override suspend fun validateTag(key: String): Boolean = backing.validateTag(key)
    override fun get(vararg keys: String?): TemplateRenderer? = backing.get(*keys)
}

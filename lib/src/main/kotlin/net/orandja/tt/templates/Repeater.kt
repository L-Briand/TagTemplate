package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer

class Repeater(
    private val times: Int,
    private val backing: TemplateRenderer
) : TemplateRenderer() {
    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean = (0 until times).all {
        val result = backing.render(key, mergeContexts(contexts), onNew)
        (context as? Roller)?.increment()
        result
    }

    override var context: TemplateRenderer? = null
        set(value) {
            if (value is Roller) value.autoRoll = false
            field = value
        }

    override fun duplicate(): TemplateRenderer = Repeater(times, backing.duplicate())
    override suspend fun validateTag(key: String): Boolean = backing.validateTag(key)
    override fun get(vararg keys: String?): TemplateRenderer? = backing.get(*keys)

    override fun toString(): String = "(x$times){$backing}$contextString"
}

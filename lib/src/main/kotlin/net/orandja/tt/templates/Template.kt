package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer
import net.orandja.tt.utils.SourceTemplate
import net.orandja.tt.utils.TemplatePart

class Template constructor(
    private val source: SourceTemplate,
) : TemplateRenderer() {

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean {
        val ctxs = mergeContexts(contexts)
        // search in context to render the specified key
        if (key != null) return ctxs.firstOrNull { it.validateTag(key) }?.render(key, ctxs, onNew) ?: false
        val raw = source.templateSource
        for (part in source.parts) {
            when (part) {
                is TemplatePart.Chunk -> onNew(raw.subSequence(part.at, part.at + part.len))
                is TemplatePart.Tag -> {
                    val renderer = ctxs.firstOrNull { it.validateTag(part.tag) }
                        ?: throw IllegalStateException("tag \"${part.tag}\" not found")
                    renderer.render(part.tag, ctxs, onNew)
                }
            }
        }
        return true
    }

    override fun duplicate(): TemplateRenderer = Template(source)
    override suspend fun validateTag(key: String): Boolean = false
    override fun toString(): String = """'${source.templateSource}'$contextString"""
}

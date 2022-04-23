package net.orandja.templator.tt

import net.orandja.templator.TemplateRenderer

class Group(
    private val renders: Map<String, TemplateRenderer>
) : TemplateRenderer {
    override fun toString(): String =
        "group { ${renders.entries.joinToString { "${it.key}:${it.value}" }} }"

    override suspend fun render(key: String?, context: TemplateRenderer, onNew: (CharSequence) -> Unit): Boolean =
        renders[key]?.render(null, context, onNew) ?: false

    override fun get(vararg keys: String?): TemplateRenderer =
        if(keys.isEmpty()) this else renders[keys[0]]!!.get(*keys.sliceArray(1 until keys.size))
}
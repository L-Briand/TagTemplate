package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer

class Roller(
    private vararg val renders: TemplateRenderer
) : TemplateRenderer {
    var idxes = mutableMapOf<String?, Int>()

    override fun toString(): String =
        "roll { ${renders.joinToString { "$it" }} }"

    override suspend fun render(key: String?, context: TemplateRenderer, onNew: (CharSequence) -> Unit): Boolean =
        if (renders.isEmpty()) false
        else {
            val idx = idxes[key] ?: 0
            renders[idx].render(key, context, onNew).also {
                idxes[key] = if (idx + 1 >= renders.size) 0 else idx + 1
            }
        }

    override fun get(vararg keys: String?): TemplateRenderer =
        if (keys.isEmpty()) this else renders[keys[0]!!.toInt()].get(*keys.sliceArray(1 until keys.size))
}

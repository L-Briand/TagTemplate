package net.orandja.tt.block

import net.orandja.tt.TT
import net.orandja.tt.TemplateRenderer
import net.orandja.tt.templates.Delimiters

data class Block(val content: CharSequence, val children: Map<String, Block>? = null) {
    operator fun get(vararg keys: String?): CharSequence =
        if (keys.isEmpty()) content
        else children?.get(keys[0])?.get(*keys.sliceArray(1 until keys.size))
            ?: throw IllegalStateException("keys: $keys not found")

    /**
     * Create a map containing all templates in it.
     * In this map the key is from the name of the template
     * e.g.
     * """-−{ first bob --{ second alice }--}--"""
     * flatten()[".first"].renderToString() == bob
     * flatten()[".first.second"].renderToString == alice
     */
    fun asTemplateMap(
        separator: String = Delimiters.DEFAULT_SEPARATOR,
        delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE
    ): Map<String, TemplateRenderer> =
        mutableMapOf<String, TemplateRenderer>().apply { flatten("", separator, this, delimiters) }

    private fun flatten(
        key: String,
        separator: String,
        map: MutableMap<String, TemplateRenderer>,
        delimiters: Delimiters
    ) {
        map[key] =
            if (content.contains(delimiters.start) && content.contains(delimiters.end))
                TT.template(content, delimiters)
            else TT.value(content)
        if (children == null) return
        for (child in children) {
            child.value.flatten("$key$separator${child.key}", separator, map, delimiters)
        }
    }
}
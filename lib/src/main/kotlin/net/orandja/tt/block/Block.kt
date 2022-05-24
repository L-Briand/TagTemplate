package net.orandja.tt.block

import net.orandja.tt.TT
import net.orandja.tt.TemplateRenderer
import net.orandja.tt.templates.Delimiters

data class Block(val content: CharSequence, val children: Map<String, Block>? = null) {
    operator fun get(vararg keys: String?, delimiters: Delimiters = Delimiters.DEFAULT_TEMPLATE): TemplateRenderer =
        if (keys.isEmpty()) TT.template(content, delimiters)
        else children?.get(keys[0])?.get(*keys.sliceArray(1 until keys.size), delimiters = delimiters)
            ?: throw IllegalStateException("keys: $keys not found")
}
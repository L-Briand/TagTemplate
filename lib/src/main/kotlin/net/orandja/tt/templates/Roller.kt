package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer

class Roller(
    private vararg val renders: TemplateRenderer
) : TemplateRenderer() {

    private val idxes = mutableMapOf<String?, Int>()

    override fun toString(): String =
        "roll { ${renders.joinToString { "$it" }} }"

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean = if (renders.isEmpty()) false else {
        val idx = idxes[key] ?: 0
        val result = idx.let(renders::get).render(key, mergeContexts(contexts), onNew)
        idxes[key] = if (idx + 1 >= renders.size) 0 else idx + 1
        result
    }

    override fun clone(): TemplateRenderer = Roller(*renders.map { it.clone() }.toTypedArray())
    override suspend fun validateTag(key: String): Boolean =
        (idxes[key] ?: 0).let(renders::get).validateTag(key)

    override fun getExternalTemplate(key: String): TemplateRenderer? =
        key.toIntOrNull()?.let { idxes[key] ?: 0 }?.let(renders::get)
}

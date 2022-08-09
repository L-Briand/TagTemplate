package net.orandja.tt.templates

import net.orandja.tt.TemplateRenderer

class Roller(
    private vararg val renders: TemplateRenderer,
    var autoRoll: Boolean = true,
) : TemplateRenderer() {

    private val autoRollIdx = mutableMapOf<String?, Int>()
    private var rollIdx = 0

    fun increment() {
        rollIdx++
        if (rollIdx >= renders.size) rollIdx = 0
    }

    private fun currentIdx(key: String? = null) = if (autoRoll) autoRollIdx[key] ?: 0 else rollIdx

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean = if (renders.isEmpty()) false else {
        val idx = currentIdx(key)
        val result = idx.let(renders::get).render(key, mergeContexts(contexts), onNew)
        if (autoRoll) autoRollIdx[key] = if (idx + 1 >= renders.size) 0 else idx + 1
        result
    }

    override fun duplicate(): TemplateRenderer = Roller(*renders.map { it.duplicate() }.toTypedArray())
    override suspend fun validateTag(key: String): Boolean = currentIdx(key).let(renders::get).validateTag(key)
    override fun getExternalTemplate(key: String): TemplateRenderer? = key.toIntOrNull()?.let(renders::get)
    override fun toString(): String = "[${renders.joinToString(",") { "$it" }}]$contextString"
}

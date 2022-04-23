package net.orandja.templator

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

interface TemplateRenderer {
    suspend fun render(key: String?, context: TemplateRenderer, onNew: (CharSequence) -> Unit): Boolean
    fun get(vararg keys: String?): TemplateRenderer
    fun context(): TemplateRenderer? = null
}

inline fun <reified T : TemplateRenderer> T.renderFlow(key: String? = null): Flow<CharSequence> = flow {
    render(key, this@renderFlow) { runBlocking { emit(it) } }
}

inline fun <reified T : TemplateRenderer> T.renderToString(key: String? = null): String = runBlocking {
    val builder = StringBuilder()
    render(key, this@renderToString) { builder.append(it) }
    builder.toString()
}
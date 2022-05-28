package net.orandja.tt

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

abstract class TemplateRenderer {

    /**
     * Search into [contexts] or its own [context] to render a template.
     * An implementation must provide part of rendered template by the [onNew] lambda.
     * The [key] can be used to select a template to draw.
     */
    abstract suspend fun render(key: String?, contexts: Array<TemplateRenderer>, onNew: (CharSequence) -> Unit): Boolean

    /** Duplicate the template */
    abstract fun duplicate(): TemplateRenderer

    /** Verify that a tag can be renderer with this template */
    abstract suspend fun validateTag(key: String): Boolean

    open var context: TemplateRenderer? = null
    infix fun bindTo(context: TemplateRenderer?) = apply { this.context = context }

    /** Allow for depth getter of template */
    open operator fun get(vararg keys: String?): TemplateRenderer? {
        val result =
            if (keys.isEmpty()) this
            else keys[0]?.let(::getExternalTemplate)?.get(*keys.sliceArray(1 until keys.size))
        return result?.duplicate()
    }

    protected open fun getExternalTemplate(key: String): TemplateRenderer? = context?.get(key)

    protected fun mergeContexts(contexts: Array<TemplateRenderer>): Array<TemplateRenderer> {
        val localContext = context
        return if (localContext == null) contexts
        else arrayOf(localContext) + contexts
    }
}

inline fun <reified T : TemplateRenderer> T.renderFlow(key: String? = null): Flow<CharSequence> = flow {
    render(key, arrayOf(this@renderFlow)) { runBlocking { emit(it) } }
}

inline fun <reified T : TemplateRenderer> T.renderToString(key: String? = null): String = runBlocking {
    val builder = StringBuilder()
    render(key, arrayOf(this@renderToString)) { builder.append(it) }
    builder.toString()
}

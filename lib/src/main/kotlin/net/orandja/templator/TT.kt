package net.orandja.templator

object TT {
    @JvmStatic
    fun template(str: CharSequence, delimiters: Delimiters = Delimiters.DEFAULT) = Template(str, delimiters)

    @JvmStatic
    fun value(str: CharSequence) = TemplateRenderer { _, _, onNew ->
        onNew(str)
        true
    }

    @JvmStatic
    fun repeat(times: Int, templateRenderer: TemplateRenderer) = TemplateRenderer { key, context, onNew ->
        (0 until times).all { templateRenderer.render(key, context, onNew) }
    }

    @JvmStatic
    fun group(vararg elements: Pair<String, TemplateRenderer>) = group(elements.toMap())

    @JvmStatic
    fun group(elements: Map<String, TemplateRenderer>) = TemplateRenderer { key, context, onNew ->
        elements[key]?.render(null, context, onNew) ?: false
    }

    @JvmStatic
    fun roll(elements: Iterable<TemplateRenderer>) = roll(*elements.toList().toTypedArray())

    @JvmStatic
    fun roll(vararg elements: TemplateRenderer) = object : TemplateRenderer {
        var idxes = mutableMapOf<String?, Int>()
        override suspend fun render(key: String?, context: TemplateRenderer, onNew: (CharSequence) -> Unit): Boolean =
            if (elements.isEmpty()) false
            else {
                val idx = idxes[key] ?: 0
                elements[idx].render(key, context, onNew).also {
                    idxes[key] = if (idx + 1 >= elements.size) 0 else idx + 1
                }
            }
    }

    @JvmStatic
    fun onContext(context: TemplateRenderer, onto: TemplateRenderer) = TemplateRenderer { key, _, onNew ->
        onto.render(key, context, onNew)
    }
}

package net.orandja.tt

inline infix fun <reified T : Any> TemplateRenderer.bindToData(data: T) = this bindTo TR.makeTemplate(data)
inline infix fun <reified T : TemplateRenderer> TemplateRenderer.bindToList(data: List<T>) = TT.list(this, data)
inline infix fun <reified T : Any> TemplateRenderer.bindToDataList(data: List<T>) =
    this bindToList data.map { TR.makeTemplate(it) }

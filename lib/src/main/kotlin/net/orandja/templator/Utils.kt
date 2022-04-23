package net.orandja.templator

import kotlin.reflect.full.memberProperties

inline fun <reified T : Any> T.reflectMap(): Map<String, Any?> {
    return T::class.memberProperties.associate { it.name to it.get(this) }
}

inline fun <reified T> Map<String, T>.asValues() = entries.associate {
    it.key to TT.value(it.value.toString())
}

inline fun <reified T : Any> T.asGroup() = TT.group(reflectMap().asValues())
inline fun <reified T : TemplateRenderer> T.onContext(context: TemplateRenderer) = TT.onContext(this, context)
inline fun <reified T : Any> List<T>.asRoll() = TT.roll(map { it.asGroup() })

/** Transform any class into a renderer */
inline fun <reified T : Any> T.asTemplateRender(template: TemplateRenderer) =
    template.onContext(asGroup())

/** Transform any List into a renderer */
inline fun <reified T : Any> List<T>.asTemplateRender(template: TemplateRenderer) =
    TT.onContext(TT.repeat(size, template), this.asRoll())

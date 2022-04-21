package net.orandja.templator

import kotlin.reflect.full.memberProperties

inline fun <reified T : Any> T.reflectMap(): Map<String, Any?> {
    return T::class.memberProperties.associate { it.name to it.get(this) }
}

inline fun <reified T> Map<String, T>.asValues() = this.entries.associate {
    it.key to TT.value(it.value.toString())
}

inline fun <reified T : Any> T.asGroup() = TT.group(reflectMap().asValues())

inline fun <reified T : Any> List<T>.asRoll() = TT.roll(this.map { it.asGroup() })
inline fun <reified T : Any> List<T>.rollWithTemplate(template: TemplateRenderer) =
    TT.onContext(this.asRoll(), TT.repeat(this.size, template))

inline fun <reified T : TemplateRenderer> T.onContext(context: TemplateRenderer) = TT.onContext(context, this)

inline fun <reified T : TemplateRenderer> Iterable<T>.roll() = TT.roll(this)

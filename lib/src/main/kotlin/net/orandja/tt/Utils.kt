package net.orandja.tt

import kotlin.reflect.full.memberProperties

inline fun <reified T : Any> T.reflectMap(): Map<String, Any?> {
    return T::class.memberProperties.associate { it.name to it.get(this) }
}

inline fun <reified T> Map<String, T>.asValues() = entries.associate {
    it.key to TT.value(it.value.toString())
}

inline fun <reified T : Any> T.asKeyValueGroup() = TT.group(reflectMap().asValues())
inline fun <reified T : Any> List<T>.asRoll() = TT.roll(map { it.asKeyValueGroup() })

inline infix fun <reified T : Any> TemplateRenderer.bindToData(data: T) =
    clone() bindTo data.asKeyValueGroup()

inline infix fun <reified T : Any> TemplateRenderer.bindToList(data: List<T>) =
    TT.repeat(data.size, this) bindTo data.asRoll()

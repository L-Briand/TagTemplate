package net.orandja.tt

import kotlin.reflect.full.memberProperties

inline fun <reified T : Any> T.reflectMap(): Map<String, Any?> {
    return T::class.memberProperties.associate { it.name to it.get(this) }
}

inline fun <reified T> Map<String, T>.toTemplateValue() = entries.associate {
    it.key to TT.value(it.value.toString())
}

inline fun <reified T : Any> T.toKeyValueTemplate() = TT.templates(reflectMap().toTemplateValue())
inline fun <reified T : Any> List<T>.toTemplateRoll() = TT.roll(map { it.toKeyValueTemplate() })

inline fun <reified T> List<T>.makeTemplateValue(key: String) = map { TT.templates(key to TT.value("$it")) }
inline fun <reified T> List<T>.makeTemplate(key: String, action: (T) -> TemplateRenderer): List<TemplateRenderer> =
    map { TT.templates(key to action(it)) }

inline infix fun <reified T : Any> TemplateRenderer.bindToData(data: T) =
    duplicate() bindTo data.toKeyValueTemplate()

inline infix fun <reified T : Any> TemplateRenderer.bindToDataList(data: List<T>) =
    TT.repeat(data.size, this) bindTo data.toTemplateRoll()

inline infix fun <reified T : TemplateRenderer> TemplateRenderer.bindToList(data: List<T>) = TT.list(this, data)

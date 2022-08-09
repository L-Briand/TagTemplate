package net.orandja.tt

import kotlin.reflect.full.memberProperties

class TR {
    companion object {

        /**
         * Transform [data] to a [Map] from its fields
         *
         * Usage :
         *   data class MyData(val data: String)
         *   val map = TR.reflectMap(MyData("hello"))
         *   map["data"] == "hello"
         */
        @JvmStatic
        inline fun <reified T : Any> reflectMap(data: T): Map<String, Any?> =
            T::class.memberProperties.associate { it.name to it.get(data) }

        /** Transform a key value map to a valid group TemplateRenderer. */
        @JvmStatic
        inline fun <reified T> toTemplateValue(data: Map<String, T>): TemplateRenderer =
            TT.values(data.mapValues { it.value.toString() })

        /** Transform any Simple object to a TemplateRenderer */
        @JvmStatic
        inline fun <reified T : Any> makeTemplate(data: T) = toTemplateValue(reflectMap(data))

        /** Build templates from any iterable */
        @JvmStatic
        inline fun <reified T> makeTemplate(
            key: String,
            on: Iterable<T>,
            action: (T) -> TemplateRenderer
        ): List<TemplateRenderer> = on.map { TT.templates(key to action(it)) }
    }
}

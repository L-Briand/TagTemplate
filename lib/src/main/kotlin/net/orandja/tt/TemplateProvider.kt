package net.orandja.tt

interface TemplateProvider {
    operator fun get(key: String?): TemplateRenderer?
    fun keys(): Set<String>
    fun clone(): TemplateProvider
}

fun Map<String, TemplateRenderer>.asTemplateProvider(): TemplateProvider = object : TemplateProvider {
    val backing = this@asTemplateProvider
    override fun get(key: String?): TemplateRenderer? = backing[key]
    override fun keys(): Set<String> = backing.keys
    override fun clone(): TemplateProvider =
        backing.entries.associate { it.key to it.value.clone() }.asTemplateProvider()
}

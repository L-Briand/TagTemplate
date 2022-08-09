package net.orandja.tt

/** Something that has templates, used by TT.group */
interface TemplateProvider {
    operator fun get(key: String): TemplateRenderer?
    fun keys(): Set<String>
    fun duplicate(): TemplateProvider
}

fun Map<String, TemplateRenderer>.asTemplateProvider(): TemplateProvider = object : TemplateProvider {
    val backing = this@asTemplateProvider
    override fun get(key: String): TemplateRenderer? = backing[key]
    override fun keys(): Set<String> = backing.keys
    override fun duplicate(): TemplateProvider =
        backing.entries.associate { it.key to it.value.duplicate() }.asTemplateProvider()
}

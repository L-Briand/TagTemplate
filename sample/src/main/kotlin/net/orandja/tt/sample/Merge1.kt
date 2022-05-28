package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.TemplateRenderer
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun merge1() {
    // Merging templates is the act of taking multiples templates and
    // putting them on the same level. Making one available to another
    // without setting a context.
    val descriptive = TT.template("This is {{ component/name }}")
    val chair = TT.values("name" to "a chair")
    val table = TT.values("name" to "a table")

    var renderer: TemplateRenderer

    // There is two possible outcomes when merging:
    // - The merge template has keys, each key is prepended by the tag
    // - The merge template isn't a group, the whole template is behind the tag
    renderer = TT.merge(
        "main" to descriptive,
        "component" to chair,
    )

    // renderer is now a group containing two keys "main/" and "component/name".
    // The "descriptive" template search for tag "component/name" and the merge
    // has created it.
    // The "/" is the separator added between the new added tag and templates.

    // It's as easy as before to render :
    println(renderer)
    assertEqual("This is a chair", renderer.renderToString("main/"))
    assertEqual("a chair", renderer.renderToString("component/name"))

    // It opens up new possibility into how to render
    renderer = TT.merge(
        "main" to descriptive,
        "component" to table,
    )
    assertEqual("This is a table", renderer.renderToString("main/"))
    assertEqual("a table", renderer.renderToString("component/name"))

}
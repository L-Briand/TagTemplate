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

    // There is two possible outcomes when merging:
    // 1. The merge template is a group, each key inside the group is prepended by the tag
    // 2. The merge template isn't a group, the whole template is behind the tag
    // Here descriptive fall under the 2. and chair under the 1.
    var renderer: TemplateRenderer = TT.group(
        "main" to descriptive,
        "component" to chair,
    )

    // renderer is now a group containing two keys "main/" and "component/name".
    // The "descriptive" template search for tag "component/name" and the merge
    // has created it.
    // The "/" is the separator added between the new added tag and templates.c
    // Separator can be set by naming parameter

    // It's as easy as before to render :
    assertEqual("This is a chair", renderer.renderToString("main/"))
    assertEqual("a chair", renderer.renderToString("component/name"))

    // It opens up new possibility on how to render things
    // The template have now changed just by having a full template changed.
    renderer = TT.group(
        "main" to descriptive,
        "component" to table,
    )
    assertEqual("This is a table", renderer.renderToString("main/"))
    assertEqual("a table", renderer.renderToString("component/name"))
}

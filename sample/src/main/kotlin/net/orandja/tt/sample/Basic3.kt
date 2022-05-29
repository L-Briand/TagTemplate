package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun basic3() {
    // When creating a template you may want to get an unknown resource.
    // Here "name" is undefined. It will be searched when rendered.
    val tt1 = TT.template("i'm {{ name }}")

    // Maybe this template is used somewhere else.
    // "name" is still not defined.
    val tt2 = TT.template("Hello {{ person_greeting }} !") bindTo TT.templates("person_greeting" to tt1)

    // And then we finally define it.
    val tt3 = TT.templates(
        "hello" to tt2,
        "name" to TT.value("mike"),
    )

    // Even if a tag is n'th layer above. Contexts are propagated to children when rendering.
    assertEqual("Hello i'm mike !", tt3.renderToString("hello"))
    // However, without definition it will not work
    val error = try {
        tt2.renderToString()
    } catch (e: IllegalStateException) {
        e.message
    }
    assertEqual("tag {{ name }} not found", error)

    // If the tag is already set on a lower template it has priority
    // over a tag defined in an upper layer. Here we force the person
    // template to have a fixed name.
    tt1 bindTo TT.templates("name" to TT.value("jon"))
    assertEqual("Hello i'm jon !", tt3.renderToString("hello"))

    // To sum up, the most near-by tag in the depth tree is always used.
    // if we set a name in-between, (on tt2) it will use its value instead of tt3 value
    tt1 bindTo null
    tt2 bindTo TT.templates(
        "person_greeting" to tt1,
        "name" to TT.value("elvis"),
    )
    assertEqual("Hello i'm elvis !", tt3.renderToString("hello"))
}

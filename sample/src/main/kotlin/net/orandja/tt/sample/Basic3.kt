package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun basic3() {
    // When creating a template you may want to get an unknown resource.
    // Here "name" is undefined. It will be searched when rendered.
    val person = TT.template("i'm {{ name }}")

    // Maybe this template is used somewhere else.
    // "name" is still not defined.
    val hello = TT.template("Hello {{ something }} !") bindTo TT.group("something" to person)

    // And then we finally define it.
    val printer = TT.group(
        "hello" to hello,
        "name" to TT.value("mike"),
    )

    // Even if a tag is n'th layer above. Contexts are propagated to children when rendering.
    assertEqual("Hello i'm mike !", printer.renderToString("hello"))
    // However, without definition it will not work
    val error = try {
        hello.renderToString()
    } catch (e: IllegalStateException) {
        e.message
    }
    assertEqual("{{ name }} not found", error)

    // A bind has priority over a tag defined in an upper layer.
    // Here we force the person template to have a fixed name.
    person bindTo TT.group("name" to TT.value("jon"))
    assertEqual("Hello i'm jon !", printer.renderToString("hello"))

    // Finally, the most near-by tag in the depth tree is always used.
    // if we set a name in-between, (on hello) it will use its name instead of mike
    person bindTo null
    hello bindTo TT.group(
        "something" to person,
        "name" to TT.value("elvis"),
    )
    assertEqual("Hello i'm elvis !", printer.renderToString("hello"))
}

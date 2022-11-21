package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun basic2() {
    // Given :
    val userTemplate = TT.template("( {{ lastName }} - {{ firstName }} ),")

    // It's possible to bind a template on any template with "bindTo".
    // This act as a context for keys to search.
    val template = userTemplate.bindTo(
        TT.templates(
            "firstName" to TT.value("Sherill"),
            "lastName" to TT.value("Auston"),
        )
    )
    // Now userTemplate is bound to a context with firstName and lastName known.

    // It renders correctly without specifying a name because userTemplate do not have a name.
    assertEqual("( Auston - Sherill ),", template.renderToString())
    // And you can still render its underlying information with the context
    assertEqual("Sherill", template.context!!.renderToString("firstName"))
    // Even without the context it'll try to get the corresponding key automatically
    assertEqual("Sherill", template.renderToString("firstName"))

    // "bindTo" is an infix function for cleaner code.
    val template2 = userTemplate bindTo TT.templates(
        "firstName" to TT.value("Sherill"),
        "lastName" to TT.value("Auston"),
    )

    // For simple key/values strings you can simplify the writing with TT.values
    val template3 = userTemplate bindTo TT.values(
        "firstName" to "Sherill",
        "lastName" to "Auston",
    )

    // When duplicating a template, you do not clone its context
    // Also template creation calculations aren't redo
    val template4 = template.duplicate()
    val error = try {
        template4.renderToString()
    } catch (e: IllegalStateException) {
        e.message
    }
    assertEqual("tag \"lastName\" not found", error)

    // You can unbind any context
    userTemplate bindTo null
}

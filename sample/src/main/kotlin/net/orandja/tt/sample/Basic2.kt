package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun basic2() {
    val userTemplate = TT.template("( {{ lastName }} - {{ firstName }} ),")

    // You can set a context on a template to bind its values without making a group.
    val template = userTemplate.bindTo(
        TT.group(
            "firstName" to TT.value("Sherill"),
            "lastName" to TT.value("Auston"),
        )
    )

    // Now it renders correctly even without a name
    assertEqual("( Auston - Sherill ),", template.renderToString())
    // And you can still render its underlying information
    assertEqual("Sherill", template.context!!.renderToString("firstName"))
    assertEqual("Sherill", template.renderToString("firstName"))

    // bind is an infix fun for cleaner code.
    val template2 = userTemplate bindTo TT.group(
        "firstName" to TT.value("Sherill"),
        "lastName" to TT.value("Auston"),
    )

    // When cloning a template, you do not clone its context
    val template3 = template2.clone()
    val error = try {
        template3.renderToString()
    } catch (e: IllegalStateException) {
        e.message
    }
    assertEqual("{{ lastName }} not found", error)

    // You can also unbind any context
    userTemplate bindTo null
}

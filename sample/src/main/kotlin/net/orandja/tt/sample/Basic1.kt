package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString
import net.orandja.tt.templates.Delimiters

fun basic1() {

    // You can create a template that search for keys
    val template = TT.template("{{ lastName }} - {{ firstName }}")
    // Or create a value template that will just print its content.
    val value = TT.value("{{ lastName }}") // Will just print "{{ lastName }}" when rendered

    // You can change delimiters on a template.
    // By default delimiters are {{ and }}
    val delimiters = Delimiters("[[", "]]")
    val template2 = TT.template("[[ lastName ]] - [[ firstName ]]", delimiters)

    // With TT.templates you can create a group containing everything you need.
    val templates = TT.templates(
        "userTemplate" to TT.template("{{ lastName }} - {{ firstName }}"),
        "firstName" to TT.value("Sherill"),
        "lastName" to TT.value("Auston"),
    )

    // then you can render anything inside this group
    assertEqual("Auston - Sherill", templates.renderToString("userTemplate"))
    assertEqual("Sherill", templates.renderToString("firstName"))
}

package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun basic4() {
    val myTemplate = "{{ lastName }} - {{ firstName }}"

    // You can read a template from any stream
    val template = TT.templateStream(
        // This lambda indicates the time at which the stream was last modified
        lastUpdate = { -1 },
        source = { myTemplate.byteInputStream() }
    )

    // Same goes to values
    template bindTo TT.templates(
        "firstName" to TT.valueStream { "Sherill".byteInputStream() },
        "lastName" to TT.valueStream { "Auston".byteInputStream() },
    )

    assertEqual("Auston - Sherill", template.renderToString())
}

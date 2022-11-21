package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString
import net.orandja.tt.utils.SourceStream

fun basic4() {
    val myTemplate = "{{ lastName }} - {{ firstName }}"

    // You can read a template from any stream
    val template = TT.templateStream(
        SourceStream.Static(-1, myTemplate.byteInputStream())
    )

    // Same goes to values
    template bindTo TT.templates(
        "firstName" to TT.valueStream { "Sherill".byteInputStream() },
        "lastName" to TT.valueStream { "Auston".byteInputStream() },
    )

    assertEqual("Auston - Sherill", template.renderToString())
}

package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun basic4() {
    val myTemplate = "{{ lastName }} - {{ firstName }}"

    // You can read a template from any stream
    val template = TT.templateStream(
        sourceLastUpdate = { -1 },
        source = { myTemplate.byteInputStream() }
    )

    template bindTo TT.values(
        "firstName" to "Sherill",
        "lastName" to "Auston",
    )

    assertEqual("Auston - Sherill", template.renderToString())
}

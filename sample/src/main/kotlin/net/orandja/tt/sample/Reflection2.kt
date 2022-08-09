package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.bindToDataList
import net.orandja.tt.renderToString

fun reflection2() {
    val users = listOf(
        UserInformation("Auston", "Sherill"),
        UserInformation("Marinda", "Abbi"),
        UserInformation("Tolly ", "Sheila"),
    )
    val userTemplate = TT.template("( {{ lastName }} - {{ firstName }} ),")

    // To make things more concise, like a template group from a data class
    // We can create a dynamic template from any List<T>.
    val template = userTemplate bindToDataList users

    assertEqual("( Auston - Sherill ),( Marinda - Abbi ),( Tolly  - Sheila ),", template.renderToString())
}

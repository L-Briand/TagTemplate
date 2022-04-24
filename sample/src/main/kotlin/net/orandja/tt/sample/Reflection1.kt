package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.asGroup
import net.orandja.tt.asTemplate
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun reflection1() {
    val user1 = UserInformation("Auston", "Sherill")
    val userTemplate = TT.template("( {{ lastName }} - {{ firstName }} ),")

    // you can use reflection to create template group more easily.
    // Beware of proguard, R8 or other tools that obfuscate code.
    // This is the same as TT.group("firstName" to user1.firstName, "lastName" to user1.lastName)
    val userInfo = user1.asGroup()

    // It simplifies the creation of contextual templates
    val contextualizedUserInfo = userTemplate.clone().bindTo(userInfo) // same as basic2 example

    // To make it even more accessible any class can be transformed to a contextualized group
    // It binds automatically userTemplate to user1.
    val template = user1.asTemplate(userTemplate) // same as last two lines

    assertEqual("( Auston - Sherill ),", template.renderToString())
}

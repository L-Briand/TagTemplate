package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.asGroup
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun list1() {
    // See reflection1
    val user1 = UserInformation("Auston", "Sherill")
    val user2 = UserInformation("Marinda", "Abbi")
    val user1Group = user1.asGroup()
    val user2Group = user2.asGroup()
    val userTemplate = TT.template("( {{ lastName }} - {{ firstName }} ),")

    // You can roll elements on a template to make it dynamic.
    // By calling multiple times the same template, it will iterate over it's key
    val templates = TT.group(
        "userTemplate" to userTemplate,
        "firstName" to TT.roll(TT.value(user1.firstName), TT.value(user2.firstName)),
        "lastName" to TT.roll(TT.value(user1.lastName), TT.value(user2.lastName)),
    )

    assertEqual("( Auston - Sherill ),", templates.renderToString("userTemplate"))
    assertEqual("( Marinda - Abbi ),", templates.renderToString("userTemplate"))
    assertEqual("Sherill", templates.renderToString("firstName"))
    assertEqual("Abbi", templates.renderToString("firstName"))

    // You can roll any TemplateRenderer (group, roll, context, etc)
    val usersRoll = TT.roll(user1Group, user2Group)

    val template = userTemplate.bindTo(usersRoll)
    assertEqual("( Auston - Sherill ),", template.renderToString())
    assertEqual("( Marinda - Abbi ),", template.renderToString())

    // Internally it rolls on keys and not on group of elements.
    // Changing template in middle of a roll can make things strange.
    val onlyFirstName = TT.template("{{firstName}}").bindTo(usersRoll)
    assertEqual("Sherill", onlyFirstName.renderToString()) // prints "Sherill"
    assertEqual("( Auston - Abbi ),", template.renderToString()) // prints
    // In the second prints it takes the first lastName and the second firstName
    // since the first firstName has already been used.

    // Note : As you have may notice rolls loop over. in this example, we have used multiple times usersRoll
}

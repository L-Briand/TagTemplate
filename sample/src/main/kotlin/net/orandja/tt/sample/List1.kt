package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.asKeyValueGroup
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun list1() {
    val user1 = UserInformation("Auston", "Sherill")
    val user2 = UserInformation("Marinda", "Abbi")
    val user1Group = user1.asKeyValueGroup() // See reflection1
    val user2Group = user2.asKeyValueGroup() // See reflection1
    val userTemplate = TT.template("( {{ lastName }} - {{ firstName }} ),")

    // You can roll elements on a template to make it dynamic.
    // By calling multiple times the same template, it will iterate over keys
    val templates = userTemplate bindTo TT.group(
        "firstName" to TT.roll(TT.value(user1.firstName), TT.value(user2.firstName)),
        "lastName" to TT.roll(TT.value(user1.lastName), TT.value(user2.lastName)),
    )

    // Rendering firstName multiples times yield different result.
    assertEqual("Sherill", templates.renderToString("firstName"))
    assertEqual("Abbi", templates.renderToString("firstName"))

    // Same goes with a whole template.
    assertEqual("( Auston - Sherill ),", templates.renderToString())
    assertEqual("( Marinda - Abbi ),", templates.renderToString())

    // You can roll any TT (template, value, group, roll, repeat)
    val usersRoll = TT.roll(user1Group, user2Group)

    val template = userTemplate bindTo usersRoll
    // The roll is done on two groups instead of 2x2 template value
    // The keys are rolled just fine.
    assertEqual("( Auston - Sherill ),", template.renderToString())
    assertEqual("( Marinda - Abbi ),", template.renderToString())

    // As said previously.
    // Internally it rolls on keys and not on group of elements.
    // Changing template in the middle of a roll can make things strange.
    // If the same roll is used for two different template it might create bugs.
    val onlyFirstName = TT.template("{{firstName}}").bindTo(usersRoll)
    assertEqual("Sherill", onlyFirstName.renderToString())
    assertEqual("( Auston - Abbi ),", template.renderToString()) // lastName from user1 firstName from user2
    // In the second prints it takes the first lastName and the second firstName
    // since the first firstName has already been used.

    // Note : As you have may notice rolls loops over.
}

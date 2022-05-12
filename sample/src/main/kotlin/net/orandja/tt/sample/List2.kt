package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.asKeyValueGroup
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun list2() {
    // See reflection1
    val user1Group = UserInformation("Auston", "Sherill").asKeyValueGroup()
    val user2Group = UserInformation("Marinda", "Abbi").asKeyValueGroup()
    val userTemplate = TT.template("( {{ lastName }} - {{ firstName }} ),")

    // If you want to render something multiple times you can repeat it.
    val template1 = TT.repeat(2, userTemplate) bindTo user1Group
    assertEqual("( Auston - Sherill ),( Auston - Sherill ),", template1.renderToString())

    // By combining it with the roll, we can make dynamic lists.
    val template2 = TT.repeat(2, userTemplate) bindTo TT.roll(user1Group, user2Group)
    assertEqual("( Auston - Sherill ),( Marinda - Abbi ),", template2.renderToString())

    // See Reflection2 for a more consise way to do that
}

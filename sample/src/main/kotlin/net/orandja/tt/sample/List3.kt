package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString
import net.orandja.tt.toKeyValueTemplate

fun list3() {
    // See reflection1
    // Given :
    val user1Group = UserInformation("Auston", "Sherill").toKeyValueTemplate()
    val user2Group = UserInformation("Marinda", "Abbi").toKeyValueTemplate()
    val userTemplate = TT.template("( {{ lastName }} - {{ firstName }} ),")

    // If you want to render something multiple times you can repeat it.
    val template1 = TT.repeat(2, userTemplate) bindTo user1Group
    assertEqual("( Auston - Sherill ),( Auston - Sherill ),", template1.renderToString())

    // By combining it with the roll, we can make dynamic lists.
    val template2 = TT.repeat(2, userTemplate) bindTo TT.roll(user1Group, user2Group)
    assertEqual("( Auston - Sherill ),( Marinda - Abbi ),", template2.renderToString())

    // As explained in List2, rolls are done with keys in mind but this combination
    // (repeat bindTo roll) triggers a particular roll behavior.
    // The roll renders now by block and not by keys.
    // You can freely use multiple times the same key and have the correct result.
    val userTemplate2 = TT.template("( {{ lastName }} - {{ lastName }} ),")
    val template3 = TT.repeat(2, userTemplate2) bindTo TT.roll(user1Group, user2Group)
    assertEqual("( Auston - Auston ),( Marinda - Marinda ),", template3.renderToString())

    // See Reflection2 for a more concise way to do that
}

package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.asGroup
import net.orandja.tt.asTemplateRender
import net.orandja.tt.renderToString
import net.orandja.tt.templates.Delimiters

data class UserInformation(val lastName: String, val firstName: String)

val user1 = UserInformation("Auston", "Sherill")
val user2 = UserInformation("Marinda", "Abbi")
val user3 = UserInformation("Tolly ", "Sheila")
val users = listOf(user1, user2, user3)

val userTemplate = TT.template("{{ lastName }} - {{ firstName }},")

// Note : any TT.template, TT.value, TT.group, TT.onContext, TT.roll, TT.repeat are TemplateRenderer.
// To create complex templates structures you should combine them.

fun basic1() {

    // You can create a template that search for keys
    val template = TT.template("{{ lastName }} - {{ firstName }}")
    // Or create a value template that will just print its content.
    val value = TT.value("{{ lastName }}") // Will just print "{{ lastName }}" when rendered

    // You can change delimiters on a template.
    val delimiters = Delimiters("[[", "]]")
    val template2 = TT.template("[[ lastName ]] - [[ firstName ]]", delimiters)

    // Create a group containing all information needed to render themselves.
    val templates = TT.group(
        "userTemplate" to template2,
        "firstName" to TT.value(user1.firstName),
        "lastName" to TT.value(user1.lastName),
    )

    // prints "Auston - Sherill"
    println(templates.renderToString("userTemplate"))
    // you can render anything in a group.
    println(templates.renderToString("firstName")) // prints "Sherill"
}

fun basic2() {
    // You can set a context on a template to bind it to values without making a group.
    val template = TT.onContext(
        userTemplate,
        TT.group(
            "firstName" to TT.value(user1.firstName),
            "lastName" to TT.value(user1.lastName),
        )
    )

    // prints "(Auston - Sherill),"
    println(template.renderToString())
    // Since it's not a group, firstName & lastName are not accessible anymore.
    // This will fail :
    // println(template.renderToString("firstName"))

    // However you can fetch the context on any template with
    val context = template.context()
    println(context.renderToString("firstName")) // prints "Sherill"
}

fun reflection1() {
    // you can uses reflection to create template group more easily.
    // Beware of proguard, R8 or other tools that obfuscate code.
    // This is the same as TT.group("firstName" to user1.firstName, "lastName" to user1.lastName)
    val userInfo = user1.asGroup()

    // It simplifies the creation of contextual templates
    val contextualizedUserInfo = TT.onContext(userTemplate, userInfo) // same as basic2 example

    // To make it even more accessible any class can be transformed to a contextualized group
    val template = user1.asTemplateRender(userTemplate) // same as last two lines

    // prints "(Auston - Sherill),"
    println(template.renderToString())
}

val user1Group = user1.asGroup()
val user2Group = user2.asGroup()
val user3Group = user3.asGroup()

fun list1() {
    // You can roll elements on a template to make it dynamic.
    // By calling multiple times the same template, it will iterate over it's key
    val templates = TT.group(
        "userTemplate" to userTemplate,
        "firstName" to TT.roll(TT.value(user1.firstName), TT.value(user2.firstName)),
        "lastName" to TT.roll(TT.value(user1.lastName), TT.value(user2.lastName)),
    )

    println(templates.renderToString("userTemplate")) // prints "(Auston - Sherill),"
    println(templates.renderToString("userTemplate")) // prints "(Marinda - Abbi),"
    println(templates.renderToString("firstName")) // prints "Sherill"
    println(templates.renderToString("firstName")) // prints "Abbi"

    // You can roll any TemplateRenderer (group, roll, context, etc)
    val usersRoll = TT.roll(user1Group, user2Group)

    val template = TT.onContext(userTemplate, usersRoll)
    println(template.renderToString()) // prints "(Auston - Sherill),"
    println(template.renderToString()) // prints "(Marinda - Abbi),"

    // Internally it rolls on keys and not on group of elements.
    // Changing template in middle of a roll can make things strange.
    val onlyFirstName = TT.onContext(TT.template("{{firstName}}"), usersRoll)
    println(onlyFirstName.renderToString()) // prints "Sherill"
    println(template.renderToString()) // prints "(Auston - Abbi),"
    // In the second prints it takes the first lastName and the second firstName
    // since the first firstName has already been used.

    // Note : As you have may notice rolls loop over.
}

fun list2() {
    // If you want to render something multiple times you can repeat it.
    val template1 = TT.onContext(
        TT.repeat(2, userTemplate),
        user1Group
    )
    // prints "(Auston - Sherill),(Auston - Sherill),"
    println(template1.renderToString())

    // By combining it with the roll, we can make dynamic lists.
    val template2 = TT.onContext(
        TT.repeat(2, userTemplate),
        TT.roll(user1Group, user2Group)
    )
    // prints "(Auston - Sherill),(Marinda - Abbi),"
    println(template2.renderToString())
}

fun reflection2() {
    // To make things more concise, like a template group from a data class
    // We can create a template dynamic from any List<T>.
    val template = users.asTemplateRender(userTemplate)

    // prints (Auston - Sherill),(Marinda - Abbi),(Tolly  - Sheila),
    println(template.renderToString())

    // here's the full transformation
    val templateExhaustive = TT.onContext(
        TT.repeat(users.size, userTemplate),
        TT.roll(users.map { it.asGroup() }) // asGroup from reflection1
    )
}

fun main() {
    println("\n--- basic1:"); basic1()
    println("\n--- basic2:"); basic2()
    println("\n--- reflection1:"); reflection1()
    println("\n--- list1:"); list1()
    println("\n--- list2:"); list2()
    println("\n--- reflection2:"); reflection2()
}

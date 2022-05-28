package net.orandja.tt.sample

import net.orandja.tt.*

fun list1() {
    // Given :
    val names = listOf("Sherill", "Abbi", "Sheila")

    // The TT system is dumb, it only map tags to values.
    // To create a list you have to create something that produce multiple items behind a tag.

    // Let say you want a list of elements in html and an element looks like this :
    val itemTemplate = TT.template("<li>{{ name }}</li>")

    // First we have to make a list of templateRenderers that can render the data
    val nameAsTTValue = names.map { TT.value(it) }
    assertEqual("Sherill", nameAsTTValue[0].renderToString())
    assertEqual("Abbi", nameAsTTValue[1].renderToString())

    // Then we need to hide it behind the tag "name" in order to make it usable for itemTemplate.
    val nameBehindTagName = nameAsTTValue.map { TT.templates("name" to it) }
    assertEqual("Sherill", nameBehindTagName[0].renderToString("name"))
    assertEqual("Abbi", nameBehindTagName[1].renderToString("name"))

    // Finally, we can assemble the list with the main template with TT.list
    val elements = TT.list(on = itemTemplate, elements = nameBehindTagName)
    // This newly created template can be rendered and print multiple elements
    assertEqual("<li>Sherill</li><li>Abbi</li><li>Sheila</li>", elements.renderToString())

    // To keep a coherent naming, you can also use bindToList instead of TT.list
    val elements3 = itemTemplate bindToList nameBehindTagName
    assertEqual("<li>Sherill</li><li>Abbi</li><li>Sheila</li>", elements3.renderToString())

    // To create your value template with any iterator<T> where T.toString() is the value you want to print
    // You can use makeTemplateValue("key")
    val namesTemplates2 = names.makeTemplateValue("name")
    // If your data is a bit more complex, let say multiple fields
    // You can use makeTemplateValue("key") { /* create a group here */ }
    val users = listOf("Will" to "Smith", "Brad" to "pitt")
    val usersTemplateBehindTag = users.makeTemplate("user") {
        TT.values(
            "firstName" to it.first,
            "lastName" to it.second,
        )
    }

    // Now we can encapsulate this into an ul element.
    val listTemplate = TT.template("<ul>{{ items }}</ul>") bindTo TT.templates("items" to elements)
    // And render it
    assertEqual("<ul><li>Sherill</li><li>Abbi</li><li>Sheila</li></ul>", listTemplate.renderToString())
}
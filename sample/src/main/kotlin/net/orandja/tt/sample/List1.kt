package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.bindToDataList
import net.orandja.tt.makeTemplate
import net.orandja.tt.makeTemplateValue
import net.orandja.tt.renderToString

fun list1() {
    val names = listOf("Sherill", "Abbi", "Sheila")

    // The TT system is dumb, it only map tags to values.
    // To create a list you have to create something that produce multiple items behind a tag.

    // Let say you want a list of elements in html and an element looks like this :
    val itemTemplate = TT.template("<li>{{ name }}</li>")

    // First we have to make a list of templateRenderers that can render our data
    val nameAsTTValue = names.map { TT.value(it) }
    assertEqual("Sherill", nameAsTTValue[0].renderToString())
    assertEqual("Abbi", nameAsTTValue[1].renderToString())

    // Then we need to hide it behind the tag "name" in order to make it usable for the main template.
    val nameBehindTagName = nameAsTTValue.map { TT.group("name" to it) }
    assertEqual("Sherill", nameBehindTagName[0].renderToString("name"))
    assertEqual("Abbi", nameBehindTagName[1].renderToString("name"))

    // Finally, we can assemble the list with the main template
    val elements = TT.list(on = itemTemplate, elements = nameBehindTagName)
    // This newly created template can be rendered and print multiple elements
    assertEqual("<li>Sherill</li><li>Abbi</li><li>Sheila</li>", elements.renderToString())

    // With a list<T>, you can create a list of TemplateRenderer more simply
    val namesTemplates1 = names.makeTemplate("name") { TT.value(it) }
    val namesTemplates2 = names.makeTemplateValue("name")

    // Now we can encapsulate this into an ul element.
    val listTemplate = TT.template("<ul>{{ items }}</ul>") bindTo TT.group("items" to elements)
    assertEqual("<ul><li>Sherill</li><li>Abbi</li><li>Sheila</li></ul>", listTemplate.renderToString())
}
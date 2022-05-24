package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.TemplateRenderer
import net.orandja.tt.assertEqual
import net.orandja.tt.block.Block
import net.orandja.tt.makeTemplateValue
import net.orandja.tt.renderToString
import net.orandja.tt.templates.Delimiters

fun parseBlock1() {
    // You can create group of templates based on a single block / file.
    // The 'item-template' block starts with a return line so each element starts with it
    val block = """
        --{ content-template
            What an awesome list :{{ items }}
        }--
        --{ item-template

            - {{ item }}
        }--
    """
    // Then you can parse it with TT.block
    val container: Block = TT.block(block)

    // And get a template
    val contentTemplate = container["content-template"]
    val itemTemplate = container["item-template"]

    // To render anything inside
    val items = listOf("apple", "tomatoes", "soap").makeTemplateValue("item")
    contentTemplate bindTo TT.group("items" to TT.list(itemTemplate, items))
    assertEqual("What an awesome list :\n- apple\n- tomatoes\n- soap", contentTemplate.renderToString())

    // You can get a template with a particular delimiter (it makes no sense here)
    val itemTemplate2 = container.get("item-template", delimiters = Delimiters("[[", "]]"))
}
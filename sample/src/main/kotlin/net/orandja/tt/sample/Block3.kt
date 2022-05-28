package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun block3() {
    // Given :
    val rawBlock = """
        <html>
            <head> {{ .headers }} </head>
            <body> {{ .body }} </body>
        </html>
        --{ headers
             <title>{{ title }}</title>
        }--
        --{ body
            <h1>{{ title }}</h1>
        }--
    """.trimIndent()

    // In parseBlock2 we have created a Map<String, TemplateRenderer>
    // You know what we can do with a Map<String, TemplateRenderer> ?
    // We can create a group with TT.templates.

    val _templates = TT.templates(TT.block(rawBlock).asTemplateMap())
    // The process of creating templates from a block can be quickened
    val templates = TT.templatesFromBlock(rawBlock)

    // By putting all of them inside a group templates now knows each other
    // and one can include another with the appropriated tag

    // This group can also be bound to a context, so some tags can be bound
    // only one time for multiple templates
    templates bindTo TT.values("title" to "MyTitle")

    // Since the root tag is an empty string, it is also transfered to the created template.
    // To render the root it is required to select the "" key
    val result = templates.renderToString("")
    // since it's a group, it requires a key to work so '""' is required when selecting the root
    assertEqual(
        """
        <html>
            <head> <title>MyTitle</title> </head>
            <body> <h1>MyTitle</h1> </body>
        </html>
        
        """.trimIndent(),
        result
    )
}
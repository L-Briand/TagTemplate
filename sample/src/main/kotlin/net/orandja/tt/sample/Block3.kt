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
    // Here .headers refer to the template headers inside the block

    // Also, a group can be bound to a context, so you can bind a tag
    // for multiple templates in one go.
    templates bindTo TT.values("title" to "MyTitle")

    // The generated group of template is the equivalent of a TT.templates("key" to TT.xyz)
    // So the root block is also behind a key. This key is an empty string.
    val result = templates.renderToString("")
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

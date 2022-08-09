package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun merge2() {
    // Let say you have multiple files containing multiple parts of your document.
    // A selection of headers to put inside your document
    val headersFile = TT.templates(
        "for-mobile" to TT.template("""<meta name="viewport" content="width=device-width, initial-scale=1">"""),
        "title" to TT.template("""<title>{{ title }}</title>"""),
    )

    // a page using this header blocks
    val htmlFile = TT.template(
        """
        <html>
            <head>
                {{ headers/title }}
                {{ headers/for-mobile }}
            </head
            <body>
                <h1>{{ title }}</h1>
            </body>
        </html>
        """.trimIndent()
    )

    // You can merge the two blocks with TT.group function.
    // By default, it prepends the keys of each map with the given name in the pair
    // and add a separator between the name and its sub blocks.
    // The default separator is "/"
    // here: for-mobile == headers/for-mobile
    val masterTemplate = TT.group(
        "html" to htmlFile,
        // the key "headers" is corresponding to the tags used in htmlFile
        "headers" to headersFile,
    )

    // And then use the two separated templates as one.
    val renderer = masterTemplate bindTo TT.values("title" to "Awesome!")
    assertEqual(
        """
        <html>
            <head>
                <title>Awesome!</title>
                <meta name="viewport" content="width=device-width, initial-scale=1">
            </head
            <body>
                <h1>Awesome!</h1>
            </body>
        </html>
        """.trimIndent(),
        renderer.renderToString("html/")
    )

    // All there is for you is to use your imagination to link templates together
}

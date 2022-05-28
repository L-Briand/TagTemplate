package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.TemplateRenderer
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun merge2() {
    // Let say you have multiple files containing multiple parts of your document.
    // A selection of headers to put inside your document
    val headersFile = TT.templatesFromBlock(
        """
        --{ for-mobile
            <meta name="viewport" content="width=device-width, initial-scale=1">
        }--
        --{ title
            <title>{{ title }}</title>
        }--
        """.trimIndent()
    )

    // a different page for mobile and
    val htmlFile = TT.templatesFromBlock(
        """
        --{ default
            <html>
                <head>
                    {{ headers/.title }}
                    {{ headers/.for-mobile }}
                </head
                <body>
                    <h1>{{ title }}</h1>
                </body>
            </html>
        }--
        """.trimIndent()
    )

    // You can merge Map<String, TemplateRenderer> with the TT.merge function.
    // By default, it prepends the keys of each map with the given name in the pair
    // and add a separator between the name and its sub blocks.
    // The default separator is "/"
    // Example here: default inside htmlFile == html/.default
    val masterTemplate = TT.merge(
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
        renderer.renderToString("html/.default")
    )

    // With a bit of automation, you can bind files to templates
    // and create a base group that works for your needs.
}
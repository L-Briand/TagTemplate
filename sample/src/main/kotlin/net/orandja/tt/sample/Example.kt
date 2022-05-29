package net.orandja.tt.sample

import net.orandja.tt.*

fun example() {
    // let's make a simple webpage
    val templates = TT.templatesFromBlock(
        """
        --{ page
            <!DOCTYPE html>
            <html>
                <head>{{ .head }}</head>
                <body>{{ .body }}</body>
            </html>
        }--
        
        --{ head

            <title>{{ title }}</title>

        }--
        
        --{ body
        
            <h1>{{ title }}</h1>
            <p> 
                Hey there, this was a fun journey through TT <br />
                There is {{ num_elements }} things to do : 
            <p>
            <ul>{{ elements }}
            </ul>
            
        }--
        
        --{ element
        
            <li> {{ name }} </li>
        }--
    """
    )

    // Given :
    val title = "An interesting title"
    val thingsToDo = listOf(
        "Star the project ?",
        "Try to use it inside your projects.",
        "Maybe helps the project by contributing !"
    )
    val numElements = thingsToDo.size.toString()

    // Create template for elements because its dynamic
    val elements = templates[".element"]!! bindToList thingsToDo.makeTemplateValue("name")

    // Add a context to the whole document
    templates bindTo TT.templates(
        "title" to TT.value(title),
        "elements" to elements,
        "num_elements" to TT.value(numElements)
    )

    // render the page
    val result = templates.renderToString(".page")
    assertEqual(
        """
            <!DOCTYPE html>
            <html>
                <head>
            <title>An interesting title</title>
            </head>
                <body>
            <h1>An interesting title</h1>
            <p> 
                Hey there, this was a fun journey through TT <br />
                There is 3 things to do : 
            <p>
            <ul>
            <li> Star the project. </li>
            <li> Try to use it inside your projects. </li>
            <li> Maybe helps the project by contributing ! </li>
            </ul>
            </body>
            </html>
        """.trimIndent(), result
    )
}

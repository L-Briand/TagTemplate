package net.orandja.tt

import net.orandja.tt.escape.HtmlEscape
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {

    data class User(val name: String)

    @Test
    fun default() {
        val render = TT("Hello {{ name }}!", "name" to "John")
        assertEquals("Hello John!", render)

        val data = mapOf("name" to "John", "ag e" to 33)
        assertEquals("John, 33 y/o", TT("{{ name }}, {{ ag e }} y/o", data))

        data class User(val name: String)
        assertEquals("User(name=John)", TT("{{ class }}", "class" to User("John")))
    }

    @Test
    fun customDelimiter() {
        val format = TT(start = "%%", stop = "%")
        val render = format("Hello %% name %!", "name" to "John")
        assertEquals("Hello John!", render)
    }

    @Test
    fun htmlEscaped() {
        val format = TT(HtmlEscape)
        val render = format("{{ script }}", "script" to "<script>alert('Hello')</script>")
        assertEquals("&lt;script&gt;alert(&#x27;Hello&#x27;)&lt;/script&gt;", render)
    }
}
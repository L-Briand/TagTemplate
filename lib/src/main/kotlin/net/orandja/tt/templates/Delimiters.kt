package net.orandja.tt.templates

data class Delimiters(
    val start: CharSequence = "{{",
    val end: CharSequence = "}}",
) {
    init {
        if (start.isEmpty()) throw IllegalStateException("start delimiter cannot be empty")
        if (end.isEmpty()) throw IllegalStateException("end delimiter cannot be empty")
    }
    companion object {
        val DEFAULT = Delimiters()
    }
}

package net.orandja.tt.templates

data class Delimiters(
    val start: CharSequence,
    val end: CharSequence,
) {
    init {
        if (start.isEmpty()) throw IllegalStateException("start delimiter cannot be empty")
        if (end.isEmpty()) throw IllegalStateException("end delimiter cannot be empty")
        if (start == end) throw IllegalStateException("start and stop delimiter must be different")
    }
    companion object {
        val DEFAULT_TEMPLATE = Delimiters("{{", "}}")
        val DEFAULT_BLOCK = Delimiters("--{", "}--")
    }
}

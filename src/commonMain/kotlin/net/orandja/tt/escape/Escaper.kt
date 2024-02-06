package net.orandja.tt.escape

fun interface Escaper {
    fun escape(charSequence: CharSequence, writer: (CharSequence) -> Unit)
}
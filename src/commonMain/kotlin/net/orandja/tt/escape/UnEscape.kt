package net.orandja.tt.escape

object UnEscape : Escaper {
    override fun escape(charSequence: CharSequence, writer: (CharSequence) -> Unit) {
        writer(charSequence)
    }
}
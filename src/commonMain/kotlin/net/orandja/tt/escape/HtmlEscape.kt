package net.orandja.tt.escape

object HtmlEscape : Escaper {

    private const val AMP = '&'
    private const val AMP_ = "&amp;"
    private const val LT = '<'
    private const val LT_ = "&lt;"
    private const val GT = '>'
    private const val GT_ = "&gt;"
    private const val D_QUOT = '"'
    private const val D_QUOT_ = "&quot;"
    private const val S_QUOT = '\''
    private const val S_QUOT_ = "&#x27;"
    private const val B_QUOT = '`'
    private const val B_QUOT_ = "&#x60;"
    private const val EQUAL = '='
    private const val EQUAL_ = "&#x3D;"


    override fun escape(charSequence: CharSequence, writer: (CharSequence) -> Unit) {
        var start = 0
        var idx = 0
        while (idx < charSequence.length) {
            when (charSequence[idx]) {
                AMP -> {
                    if (start < idx) writer(charSequence.subSequence(start, idx))
                    writer(AMP_)
                    start = idx + 1
                }

                LT -> {
                    if (start < idx) writer(charSequence.subSequence(start, idx))
                    writer(LT_)
                    start = idx + 1
                }

                GT -> {
                    if (start < idx) writer(charSequence.subSequence(start, idx))
                    writer(GT_)
                    start = idx + 1
                }

                D_QUOT -> {
                    if (start < idx) writer(charSequence.subSequence(start, idx))
                    writer(D_QUOT_)
                    start = idx + 1
                }

                S_QUOT -> {
                    if (start < idx) writer(charSequence.subSequence(start, idx))
                    writer(S_QUOT_)
                    start = idx + 1
                }

                B_QUOT -> {
                    if (start < idx) writer(charSequence.subSequence(start, idx))
                    writer(B_QUOT_)
                    start = idx + 1
                }

                EQUAL -> {
                    if (start < idx) writer(charSequence.subSequence(start, idx))
                    writer(EQUAL_)
                    start = idx + 1
                }
            }
            idx++
        }
        if (start < idx) writer(charSequence.subSequence(start, idx))
    }
}
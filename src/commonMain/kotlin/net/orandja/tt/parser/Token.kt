package net.orandja.tt.parser

/**
 * Represents a token used in parsing and rendering templates.
 *
 * @property type The type of the token.
 * @property content The content associated with the token.
 */
data class Token(val type: Int, val content: CharSequence) {

    override fun toString(): String = when (type) {
        TAG -> "{{ $content }}"
        STATIC_CONTENT -> "$content"
        else -> "?"
    }

    companion object {
        const val TAG = 0
        const val STATIC_CONTENT = 1
    }
}
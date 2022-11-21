package net.orandja.tt.utils

data class Token(val type: Int, val at: Int) {
    companion object {
        const val START = 0
        const val DELIMITER_START = 1
        const val DELIMITER_END = 2
        const val END = 3

        val start = Token(START, 0)
    }
}

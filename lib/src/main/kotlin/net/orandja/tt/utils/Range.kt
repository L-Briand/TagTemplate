package net.orandja.tt.utils

data class Range(val type: Type, val from: Int, val to: Int) {
    enum class Type { TEXT, TAG }
}

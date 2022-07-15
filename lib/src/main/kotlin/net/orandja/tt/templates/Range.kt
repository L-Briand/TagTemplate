package net.orandja.tt.templates

// This is more efficient than a sealed class.
data class Range(val type: Type, val from: Int, val to: Int) {
    enum class Type { TEXT, TAG }
}

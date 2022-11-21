package net.orandja.tt.utils

sealed class TemplatePart {
    data class Chunk(val at: Int, val len: Int) : TemplatePart()
    data class Tag(val tag: String) : TemplatePart()
}

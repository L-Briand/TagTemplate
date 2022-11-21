package net.orandja.tt.utils

interface SourceValue {
    val valueSource: CharSequence

    class Static(override val valueSource: CharSequence) : SourceValue

    class Delegate(
        private val delegate: () -> CharSequence
    ) : SourceValue {
        override val valueSource: CharSequence get() = delegate()
    }
}

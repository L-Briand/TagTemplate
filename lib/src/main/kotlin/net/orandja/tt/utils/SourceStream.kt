package net.orandja.tt.utils

import java.io.InputStream

interface SourceStream {
    val lastUpdate: Long
    val inputStream: InputStream

    class Static(override val lastUpdate: Long, override val inputStream: InputStream) : SourceStream

    class Delegate(
        private val delegate: () -> SourceStream,
    ) : SourceStream {
        override val lastUpdate: Long get() = delegate().lastUpdate
        override val inputStream: InputStream get() = delegate().inputStream
    }
}

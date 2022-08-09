package net.orandja.tt

import java.io.InputStream

interface StreamProvider {
    fun lastUpdate(): Long
    fun source(): InputStream

    companion object {
        inline operator fun invoke(
            crossinline lastUpdate: () -> Long,
            crossinline source: () -> InputStream,
        ) = object : StreamProvider {
            override fun lastUpdate(): Long = lastUpdate()
            override fun source(): InputStream = source()
        }
    }
}

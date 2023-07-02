package real.cool.screen.server.core

import android.system.Os.write
import java.io.FileDescriptor
import java.nio.ByteBuffer

class IO {
    companion object {
        fun writeFully(fd: FileDescriptor, buffer: ByteBuffer) {
            var remaining = buffer.remaining()
            while (remaining > 0) {
                val written = write(fd, buffer)
                if (written < 0) {
                    throw RuntimeException("write failed")
                }
                remaining -= written
            }
        }

        fun writeFully(fd: FileDescriptor, buffer: ByteArray, offset: Int, length: Int) {
            writeFully(fd, ByteBuffer.wrap(buffer, offset, length))
        }
    }
}
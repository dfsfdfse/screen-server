package real.cool.screen.server.core

import android.media.MediaCodec
import java.io.FileDescriptor
import java.nio.ByteBuffer

class ScreenStreamer(private val fd: FileDescriptor) {

    fun writeVideoHeader(device: Device) {
        val buffer = ByteBuffer.allocate(12)
        buffer.putInt(device.width)
        buffer.putInt(device.height)
        buffer.flip()
        IO.writeFully(fd, buffer)
    }

    fun writePacket(buffer: ByteBuffer){
        IO.writeFully(fd, buffer)
    }
}
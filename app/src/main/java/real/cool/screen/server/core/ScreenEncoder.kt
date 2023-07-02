package real.cool.screen.server.core

import android.graphics.Rect
import android.media.MediaCodec
import android.os.Build
import android.os.IBinder
import android.view.Surface
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

class ScreenEncoder(val streamer: ScreenStreamer) {
    var device: Device = Device()
    private final val stopped = AtomicBoolean()
    private final val resetCapture = AtomicBoolean()

    private fun consumeResetCapture(): Boolean {
        return resetCapture.getAndSet(false)
    }

    fun start() {
        val display = createDisplay()
        streamer.writeVideoHeader(device)
        val codec = createMediaCodec()
        val surface = codec.createInputSurface()
        var alive = false
        do {
            try {
                setDisplaySurface(display, surface)
                codec.start()
                encode(codec)
                codec.stop()
            } catch (e: Exception) {
                when (e) {
                    is IllegalStateException, is IllegalArgumentException -> {
                        alive = true
                    }
                    else -> throw e
                }
            } finally {
                codec.reset()
                surface.release()
            }
        } while (alive)
        codec.release()
    }

    fun encode(codec: MediaCodec): Boolean {
        val bufferInfo = MediaCodec.BufferInfo()
        var eof = false
        var alive = true
        while (!eof) {
            if (stopped.get()) {
                alive = false
                break
            }
            val outputId = codec.dequeueOutputBuffer(bufferInfo, -1)
            try {
                if (consumeResetCapture()) break
                eof = (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0
                if (outputId >= 0) {
                    val buffer = codec.getOutputBuffer(outputId)
                    streamer.writePacket(buffer!!)

                }
            } finally {
                if (outputId >= 0) codec.releaseOutputBuffer(outputId, false)
            }
        }
        return !eof && alive
    }

    fun createMediaCodec() = MediaCodec.createByCodecName("video/avc")

    fun createDisplay(): IBinder {
        val secure =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Build.VERSION.SDK_INT == Build.VERSION_CODES.R && "S" != Build.VERSION.CODENAME
        return SurfaceControl.createDisplay("screenStream", secure)
    }

    fun setDisplaySurface(displayToken: IBinder, surface: Surface) {
        SurfaceControl.openTransaction()
        SurfaceControl.setDisplaySurface(displayToken, surface)
        SurfaceControl.setDisplayProjection(
            displayToken,
            0,
            Rect(0, 0, device.width, device.height),
            Rect(0, 0, device.width, device.height),
        )
        SurfaceControl.closeTransaction()
    }
}
package real.cool.screen.server.core

import android.annotation.SuppressLint
import android.media.MediaFormat

data class ScreenSettings(
    var maxFpx: Int = 60,
    var videoBitRate: Int = 8000000,
    var videoCodec: VideoCodec = VideoCodec.H264,
)

enum class VideoCodec(val id: Int, val code: String, mimeType: String) {
    H264(0x68_32_36_34, "H264", MediaFormat.MIMETYPE_VIDEO_AVC),
    H265(0x68_32_36_35, "H265", MediaFormat.MIMETYPE_VIDEO_HEVC),

    @SuppressLint("InlinedApi")
    AV1(0x41_56_31, "AV1", MediaFormat.MIMETYPE_VIDEO_AV1),
}

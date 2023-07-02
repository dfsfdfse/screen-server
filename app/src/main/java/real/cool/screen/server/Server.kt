package real.cool.screen.server

import android.net.LocalServerSocket
import android.util.Log
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import real.cool.screen.server.core.ScreenEncoder
import real.cool.screen.server.core.ScreenStreamer

class Server {
    companion object {
        private const val socketName = "real.cool.screen.server"

        @OptIn(DelicateCoroutinesApi::class)
        fun start() {
            Log.e("realcool", "开始")
            GlobalScope.launch(Dispatchers.IO) {
                val videoSocket = LocalServerSocket(socketName).accept()
                val screenStreamer = ScreenStreamer(videoSocket.fileDescriptor)
                val screenEncoder = ScreenEncoder(screenStreamer)
                screenEncoder.start()

            }
        }

        fun main() {
            try {
                start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}

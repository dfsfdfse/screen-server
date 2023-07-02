package real.cool.screen.server.core

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.IBinder
import android.view.Surface

@SuppressLint("PrivateApi")
class SurfaceControl {
    companion object {
        private final val CLASS = Class.forName("android.view.SurfaceControl")

        fun openTransaction() {
            CLASS.getMethod("openTransaction").invoke(null)
        }

        fun closeTransaction() {
            CLASS.getMethod("closeTransaction").invoke(null)
        }

        fun setDisplayProjection(
            displayToken: IBinder,
            orientation: Int,
            layerStackRect: Rect,
            displayRect: Rect
        ) {
            CLASS.getMethod(
                "setDisplayProjection",
                IBinder::class.java,
                Int::class.java,
                Rect::class.java,
                Rect::class.java
            ).invoke(null, displayToken, orientation, layerStackRect, displayRect)
        }

        fun setDisplayLayerStack(displayToken: IBinder, layerStack: Int) {
            CLASS.getMethod(
                "setDisplayLayerStack",
                IBinder::class.java,
                Int::class.java
            ).invoke(null, displayToken, layerStack)
        }

        fun setDisplaySurface(displayToken: IBinder, surface: Surface){
            CLASS.getMethod(
                "setDisplaySurface",
                IBinder::class.java,
                Surface::class.java
            ).invoke(null, displayToken, surface)
        }

        fun createDisplay(name: String, secure: Boolean): IBinder {
            return CLASS.getMethod(
                "createDisplay",
                String::class.java,
                Boolean::class.java
            ).invoke(null, name, secure) as IBinder
        }

        fun destroyDisplay(displayToken: IBinder) {
            CLASS.getMethod(
                "destroyDisplay",
                IBinder::class.java
            ).invoke(null, displayToken)
        }
    }
}
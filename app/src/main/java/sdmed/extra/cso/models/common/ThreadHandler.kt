package sdmed.extra.cso.models.common

import android.os.Handler
import android.os.HandlerThread

class ThreadHandler(name: String): HandlerThread(name) {
    private lateinit var handler: Handler
    fun startThread() {
        if (!isAlive) start()
        handler = Handler(looper)
    }
    fun postTask(task: Runnable) {
        handler.post(task)
    }
    fun postTask(fn: (() -> Unit)) {
        handler.post { fn() }
    }
    fun stopThread() {
        quitSafely()
    }
}
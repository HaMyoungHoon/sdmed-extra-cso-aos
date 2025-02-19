package sdmed.extra.cso.models.common

import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.locks.ReentrantLock

class QueueLockModel<T>(threadName: String) {
    var lock = ReentrantLock()
        private set
    val queue: Queue<T> = LinkedList()
    val thread = ThreadHandler(threadName)
    init {
        thread.startThread()
    }

    fun locking() = lock.lock()
    fun unlocking() = lock.unlock()
    fun findQ(locking: Boolean = true, predicate: (T) -> Boolean): T? {
        if (locking) lock.lock()
        val ret = queue.find(predicate)
        if (locking) lock.unlock()
        return ret
    }
    fun removeQ(data: T, locking: Boolean = false): Boolean {
        if (locking) lock.lock()
        val ret = queue.remove(data)
        if (locking) lock.unlock()
        return ret
    }
    fun isEmpty(locking: Boolean = true): Boolean {
        if (locking) lock.lock()
        val ret = queue.isEmpty()
        if (locking) lock.unlock()
        return ret
    }
    fun isNotEmpty(locking: Boolean = true): Boolean {
        if (locking) lock.lock()
        val ret = queue.isNotEmpty()
        if (locking) lock.unlock()
        return ret
    }
    fun dequeue(locking: Boolean = true, fn: (() -> Unit)? = null): T {
        if (locking) lock.lock()
        val ret = queue.remove()
        fn?.invoke()
        if (locking) lock.unlock()
        return ret
    }
    fun enqueue(data: T, locking: Boolean = true, fn:(() -> Unit)? = null) {
        if (locking) lock.lock()
        queue.add(data)
        fn?.invoke()
        if (locking) lock.unlock()
    }
    fun threadStart(fn: (() -> Unit), skip: Boolean = false) {
        if (skip) {
            return
        }
        thread.postTask { fn() }
    }
    fun threadStart(r: Runnable) {
        thread.postTask(r)
    }
}
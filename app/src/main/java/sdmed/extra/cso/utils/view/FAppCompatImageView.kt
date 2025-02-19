package sdmed.extra.cso.utils.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.common.GestureFlingModel
import sdmed.extra.cso.models.common.GestureScrollModel
import sdmed.extra.cso.utils.FCoroutineUtil

class FAppCompatImageView: AppCompatImageView, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    var gestureDetector: GestureDetector? = null
    var eventListener: IAsyncEventListener? = null
    init {
        setGestureDetector()
        setThisTouchListener()
    }

    override fun onDown(e: MotionEvent): Boolean {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.DOWN, e))
        })
        return false
    }
    override fun onShowPress(e: MotionEvent) {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.SHOW_PRESS, e))
        })
    }
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.SINGLE_TAP_UP, e))
        })
        return false
    }
    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.FLING, GestureFlingModel(e1, e2, velocityX, velocityY)))
        })
        return false
    }
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.SCROLL, GestureScrollModel(e1, e2, distanceX, distanceY)))
        })
        return false
    }
    override fun onLongPress(e: MotionEvent) {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.LONG_PRESS, e))
        })
    }
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.SINGLE_TAP_CONFIRMED, e))
        })
        return false
    }
    override fun onDoubleTap(e: MotionEvent): Boolean {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.DOUBLE_TAP, e))
        })
        return false
    }
    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        FCoroutineUtil.coroutineScope({
            eventListener?.onEvent(arrayListOf(ClickEvent.DOUBLE_TAP_EVENT, e))
        })
        return false
    }
    private fun setGestureDetector() {
        gestureDetector = GestureDetector(context, this)
        gestureDetector?.setOnDoubleTapListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setThisTouchListener() {
        setOnTouchListener { _, event ->
            gestureDetector?.onTouchEvent(event)
            true
        }
    }
    enum class ClickEvent {
        DOWN,
        SHOW_PRESS,
        SINGLE_TAP_UP,
        FLING,
        SCROLL,
        LONG_PRESS,
        SINGLE_TAP_CONFIRMED,
        DOUBLE_TAP,
        DOUBLE_TAP_EVENT,
    }
}
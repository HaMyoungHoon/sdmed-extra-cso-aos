package sdmed.extra.cso.models.common

import android.view.MotionEvent

data class GestureFlingModel(
    var e1: MotionEvent?,
    var e2: MotionEvent,
    var velocityX: Float,
    val velocityY: Float,
) {
}
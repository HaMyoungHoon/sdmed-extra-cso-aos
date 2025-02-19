package sdmed.extra.cso.models.common

import android.view.MotionEvent

data class GestureScrollModel(
    var e1: MotionEvent?,
    var e2: MotionEvent,
    var distanceX: Float,
    var distanceY: Float
) {
}
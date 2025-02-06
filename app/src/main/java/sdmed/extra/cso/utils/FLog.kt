package sdmed.extra.cso.utils

import android.util.Log
import sdmed.extra.cso.bases.FMainApplication

object FLog {
    fun debug(tag: String, msg: String) {
        if (!FMainApplication.ins.isDebug()) {
            return
        }
        Log.d(tag, msg)
    }
}
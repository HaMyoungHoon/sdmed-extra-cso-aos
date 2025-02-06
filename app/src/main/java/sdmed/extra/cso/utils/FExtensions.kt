package sdmed.extra.cso.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import androidx.core.app.ActivityCompat
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.fDate.FLocalize
import sdmed.extra.cso.interfaces.IRestResult
import sdmed.extra.cso.models.DataExceptionHandler
import sdmed.extra.cso.models.RestResult
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.views.main.landing.LandingActivity
import java.util.Locale
import java.util.UUID

object FExtensions {
    fun dpToPx(context: Context, dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    fun dpToPx(context: Context, dp: Int) = dpToPx(context, dp.toFloat())

    fun getLocalize() = FLocalize.parseThis(Locale.getDefault().language)
    fun getToday() = FDateTime().setLocalize(getLocalize()).setThis(System.currentTimeMillis())
    fun getTodayString() = getToday().toString("yyyy-MM-dd")
    fun getTodayDateTimeString() = getToday().toString("yyyy-MM-dd hh:mm:ss")

    fun getUUID() = UUID.randomUUID().toString()
    fun getResult(data: String?): IRestResult {
        if (data == null) {
            return RestResult().setFail(msg = "not defined error")
        }
        val firstBrace = data.indexOf("{")
        val lastBrace = data.lastIndexOf("}")
        if (firstBrace == -1 || lastBrace == -1) {
            return RestResult().setFail(msg = data)
        }
        val iThinkItIsJson = data.substring(firstBrace, lastBrace + 1)
        return try {
            Gson().fromJson(iThinkItIsJson, (object: TypeToken<RestResult>() { }).type)
        } catch (e: Exception) {
            RestResult().setFail(msg = data)
        }
    }

    fun getNumberSuffixes(data: Long): String {
        val suffixes = listOf("", "k", "m", "b", "t")
        var value = data.toDouble()
        var suffixIndex = 0
        while (value >= 1000 && suffixIndex < suffixes.size -1) {
            ++suffixIndex
            value /= 1000
        }
        if (suffixIndex == 0) {
            return "%.0F".format(value)
        }
        return "${"%.1f".format(value)}${suffixes[suffixIndex]}"
    }
    fun getNumberSuffixes(data: Int) = getNumberSuffixes(data.toLong())

    fun getDurationToTime(data: Long): String {
        val buff = data / 1000
        if (buff < 0L) {
            return ""
        }
        if (buff == 0L) {
            return "0:00"
        }
        val hours = buff / 3600
        val minutes = (buff % 3600) / 60
        val seconds = buff % 60
        return if (hours > 0L) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
    fun isTimeString(hhMMss: String): Boolean {
        if (hhMMss.isEmpty()) return false
        if (hhMMss.replace(Regex("\\d+"), "").replace(":", "").isNotEmpty()) return false
        val split = hhMMss.split(":")
        if (split.isEmpty()) return false
        val hour = try { split[0].toInt() } catch (_: Exception) { return false }
        var min = 0
        var sec = 0
        if (split.size >= 2) min = try { split[1].toInt() } catch (_: Exception) { return false }
        if (split.size >= 3) sec = try { split[2].toInt() } catch (_: Exception) { return false }
        if ((hour + min + sec) == 0) return false
        return true
    }

    fun intervalBetweenDate(expiredDate: Long): Boolean {
        val now = System.currentTimeMillis() / 1000
        // 이거 나중에 수치 좀 바꿔야겠다
        return now - expiredDate > 0
    }
    fun tokenIntervalValid(token: String?): Boolean {
        if (token == null) return false
        try {
            if (intervalBetweenDate(JWT(token).claims[FConstants.CLAIMS_EXP]?.asLong() ?: 0)) {
                return false
            }
            return true
        } catch (_: Exception) {
            return false
        }
    }
    fun checkInvalidToken(context: Context): Boolean {
        if (FRetrofitVariable.token.isNullOrEmpty()) {
            FRetrofitVariable.token = FStorage.getAuthToken(context)
        }
        val tokenRefresh = FRetrofitVariable.token ?: return false
        return tokenIntervalValid(tokenRefresh)
    }
    fun rhsTokenIsMost(newToken: String): Boolean {
        val tokenAccess = FRetrofitVariable.token ?: return true
        return try {
            val previousLong = JWT(tokenAccess).claims["exp"]?.asLong() ?: 0
            val newLong = JWT(newToken).claims["exp"]?.asLong() ?: 0
            newLong >= previousLong
        } catch (_: Exception) {
            true
        }
    }
    fun logout(context: Context, expired: Boolean = false) {
        removeLoginData(context)
        moveToLandingActivity(context, expired)
    }
    fun removeLoginData(context: Context) {
        FRetrofitVariable.token = ""
        FStorage.removeAuthToken(context)
    }
    fun moveToLandingActivity(context: Context, expired: Boolean) {
        ActivityCompat.finishAffinity(context as Activity)
        context.startActivity(Intent(context, LandingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (expired) {
                putExtra("expired", true)
            } else {
                putExtra("logout", true)
            }
        })
    }

    suspend fun <T> restTryT(fn: suspend () -> RestResultT<T>): RestResultT<T> {
        return try {
            fn()
        } catch (e: Exception) {
            RestResultT<T>().setFail(DataExceptionHandler.handleExceptionT<T>(e))
        }
    }
    suspend fun restTry(fn: suspend () -> RestResult): RestResult {
        return try {
            fn()
        } catch (e: Exception) {
            RestResult().setFail(DataExceptionHandler.handleException(e))
        }
    }
}
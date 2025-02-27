package sdmed.extra.cso.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.fDate.FDateTime
import sdmed.extra.cso.fDate.FLocalize
import sdmed.extra.cso.interfaces.IRestResult
import sdmed.extra.cso.models.DataExceptionHandler
import sdmed.extra.cso.models.RestResult
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.utils.FImageUtils.isLocalFile
import sdmed.extra.cso.views.landing.LandingActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

object FExtensions {
    fun dpToPx(context: Context, dp: Float?): Int {
        dp ?: return Int.MIN_VALUE
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }
    fun dpToPx(context: Context, dp: Int?) = dpToPx(context, dp?.toFloat())

    fun getCalendarMinimumDate(): Calendar {
        val ret = Calendar.getInstance()
        ret.time = parseDateStringToDate("2024-12-01")
        return ret
    }
    fun getLocalize() = FLocalize.parseThis(Locale.getDefault().language)
    fun getToday() = FDateTime().setLocalize(getLocalize()).setThis(System.currentTimeMillis())
    fun getTodayString() = getToday().toString("yyyy-MM-dd")
    fun getTodayDateTimeString() = getToday().toString("yyyy-MM-dd hh:mm:ss")
    fun parseDateStringToDate(dateString: String, pattern: String = "yyyy-MM-dd"): Date {
        val ret = SimpleDateFormat(pattern, Locale.getDefault())
        return ret.parse(dateString)
    }
    fun parseDateStringToCalendar(dateString: String, pattern: String = "yyyy-MM-dd"): Calendar {
        val ret = Calendar.getInstance()
        ret.time = parseDateStringToDate(dateString, pattern)
        return ret
    }

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

    fun getMagicNumber(file: File, byteCount: Int = 8): String {
        file.inputStream().use { x ->
            val ret = ByteArray(byteCount)
            x.read(ret, 0, byteCount)
            return ret.joinToString(" ") { y -> "%02X".format(y) }
        }
    }
    fun getFileMimeType(file: File, byteCount: Int = 8): String {
        val magicNumber = getMagicNumber(file, byteCount)
        val ext = when {
            magicNumber.startsWith("50 4B 03 04") -> "file.zip"
            magicNumber.startsWith("50 4B 30 30 50 4B 03 04") -> "file.zip"
            magicNumber.startsWith("25 50 44 46") -> "file.pdf"
            magicNumber.startsWith("FF D8 FF") -> "file.jpeg"
            magicNumber.startsWith("89 50 4E 47") -> "file.png"
            magicNumber.startsWith("42 4D") -> "file.bmp"
            magicNumber.startsWith("52 49 46 46") && getMagicNumber(file, 12).contains("57 45 42 50") -> "file.webp"
            magicNumber.startsWith("66 74 79 70 68 65 69 63") -> "file.heic"
            else -> file.name ?: "file.unknown"
        }
        return FContentsType.findContentType(ext)
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

    fun regexPasswordCheck(data: String?) = data?.let { Regex(FConstants.REGEX_CHECK_PASSWORD_0).matches(it) }
}
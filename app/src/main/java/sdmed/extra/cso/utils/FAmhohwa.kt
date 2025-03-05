package sdmed.extra.cso.utils

import android.content.Context
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FExtensions.moveToLandingActivity
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object FAmhohwa {
    fun decodeUtf8(data: String): String {
        val encoded = data.toCharArray().joinToString("") {
            "%${it.code.toString(16).padStart(2, '0')}"
        }
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8.name())
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
            val previousLong = JWT(tokenAccess).claims[FConstants.CLAIMS_EXP]?.asLong() ?: 0
            val newLong = JWT(newToken).claims[FConstants.CLAIMS_EXP]?.asLong() ?: 0
            newLong >= previousLong
        } catch (_: Exception) {
            true
        }
    }
    fun logout(context: Context?, expired: Boolean = false) {
        context ?: return
        removeLoginData(context)
        moveToLandingActivity(context, expired)
    }
    fun removeLoginData(context: Context) {
        FRetrofitVariable.token = ""
        FStorage.removeAuthToken(context)
    }
    fun getUserID(context: Context): String {
        return decodeUtf8(getTokenID(context))
    }
    fun getTokenID(context: Context): String {
        val token = FRetrofitVariable.token ?: FStorage.getAuthToken(context) ?: return ""
        return try {
            JWT(token).subject ?: ""
        } catch (_: Exception) {
            ""
        }
    }
    fun getThisPK(context: Context): String {
        val token = FRetrofitVariable.token ?: FStorage.getAuthToken(context) ?: return ""
        return try {
            JWT(token).claims[FConstants.CLAIMS_INDEX]?.asString() ?: ""
        } catch (_: Exception) {
            ""
        }
    }
    fun <T> toJson(data: T): String {
        return try {
            Gson().toJson(data)
        } catch (_: Exception) {
            ""
        }
    }
    fun <T> fromJson(data: String, clazz: Class<T>): T? {
        return try {
            Gson().fromJson(data, clazz)
        } catch (_: Exception) {
            null
        }
    }
    fun urlEncoder(data: String): String {
        return URLEncoder.encode(data)
    }
}
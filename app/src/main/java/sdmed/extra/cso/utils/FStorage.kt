package sdmed.extra.cso.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import sdmed.extra.cso.bases.FConstants

object FStorage {
    fun sharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(
        FConstants.SHARED_FILE_NAME, Context.MODE_PRIVATE)
    fun cryptoSharedPreferences(context: Context) = EncryptedSharedPreferences.create(
        context,
        FConstants.SHARED_CRYPT_FILE_NAME,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getAuthToken(context: Context) = getString(context, FConstants.AUTH_TOKEN)
    fun setAuthToken(context: Context, data: String?) = data?.let { putString(context, FConstants.AUTH_TOKEN, it) }
    fun removeAuthToken(context: Context) = removeData(context, FConstants.AUTH_TOKEN)
    fun getRefreshing(context: Context) = getBool(context, FConstants.TOKEN_REFRESHING)
    fun setRefreshing(context: Context, data: Boolean?) = data?.let { putBool(context, FConstants.TOKEN_REFRESHING, it) }
    fun getHomeMenuIndex(context: Context) = getInt(context, FConstants.HOME_MENU_INDEX)
    fun setHomeMenuIndex(context: Context, data: Int?) = data?.let { putInt(context, FConstants.HOME_MENU_INDEX, it) }

    private fun getBool(context: Context, keyName: String, defData: Boolean = false) = cryptoSharedPreferences(context).getBoolean(keyName, defData)
    private fun putBool(context: Context, keyName: String, data: Boolean) = cryptoSharedPreferences(context).apply { edit { putBoolean(keyName, data) } }
    private fun getInt(context: Context, keyName: String, defData: Int = -1) = cryptoSharedPreferences(context).getInt(keyName, defData)
    private fun putInt(context: Context, keyName: String, data: Int) = cryptoSharedPreferences(context).apply { edit { putInt(keyName, data) } }
    private fun getFloat(context: Context, keyName: String, defData: Float = -1F) = cryptoSharedPreferences(context).getFloat(keyName, defData)
    private fun putFloat(context: Context, keyName: String, data: Float) = cryptoSharedPreferences(context).apply { edit { putFloat(keyName, data) } }
    private fun getString(context: Context, keyName: String, defData: String = "") = cryptoSharedPreferences(context).getString(keyName, defData)
    private fun putString(context: Context, keyName: String, data: String) = cryptoSharedPreferences(context).apply { edit { putString(keyName, data) } }
    private fun removeData(context: Context, keyName: String) = cryptoSharedPreferences(context).apply { edit { remove(keyName) } }
    inline fun <reified T: Parcelable> Intent.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key)
    }
    inline fun <reified T: Parcelable> Bundle.parcelable(intent: Intent, key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") intent.getParcelableExtra(key)
    }
}
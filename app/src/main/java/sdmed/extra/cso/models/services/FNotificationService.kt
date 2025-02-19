package sdmed.extra.cso.models.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FConstants
import androidx.core.net.toUri
import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import com.gun0912.tedpermission.coroutine.TedPermission
import sdmed.extra.cso.MainActivity
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.main.edi.ediView.EDIViewActivity

class FNotificationService(context: Context): Service(), KodeinAware {
    override val kodein: Kodein by kodein(context)
    var notificationId = 0
        private set
    private val _progressNotifyBuilder: MutableList<ProgressNotifyModel> = mutableListOf()
    private val _notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }
    override fun onBind(p0: Intent?) = null

    fun checkPermission(context: Context, fn: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(context, FConstants.NOTIFICATION_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED) {
            FCoroutineUtil.coroutineScope({
                TedPermission.create()
                    .setRationaleTitle(R.string.permit_title)
                    .setRationaleMessage(R.string.permit_notify)
                    .setDeniedTitle(R.string.cancel_desc)
                    .setDeniedMessage(R.string.permit_require)
                    .setGotoSettingButtonText(R.string.permit_setting)
                    .setPermissions(*FConstants.NOTIFICATION_PERMISSION)
                    .check()
            }, { if (it.isGranted) fn() })
        } else {
            fn()
        }
    }
    fun sendNotify(context: Context, title: String, content: String = "", notifyType: NotifyType = NotifyType.DEFAULT) {
        checkPermission(context) {
            createNotificationChannel(context, notifyType)
            sendNotification(context, title, content, notifyType)
        }
    }
    fun sendNotify(context: Context, intent: Intent, title: String, content: String = "", notifyType: NotifyType = NotifyType.DEFAULT) {
        checkPermission(context) {
            createNotificationChannel(context, notifyType)
            sendNotification(context, intent, title, content, notifyType)
        }
    }
    fun sendNotify(context: Context, notifyIndex: NotifyIndex, title: String, content: String? = null, notifyType: NotifyType = NotifyType.DEFAULT, isCancel: Boolean = false, thisPK: String = "") {
        checkPermission(context) {
            createNotificationChannel(context, notifyType)
            sendNotification(context, notifyIndex, title, content, notifyType, isCancel, thisPK)
        }
    }
    fun makeProgressNotify(context: Context, uuid: String, title: String, content: String? = null, minValue: Int = 0, maxValue: Int = 0, notifyType: NotifyType = NotifyType.DEFAULT, isCancel: Boolean = false) {
        checkPermission(context) {
            createNotificationChannel(context, notifyType)
            sendNotification(context, uuid, title, content, minValue, maxValue, notifyType, isCancel)
        }
    }
    fun progressUpdate(context: Context, uuid: String, title: String? = null, content: String = "", minValue: Int = 0, maxValue: Int = 0, notifyType: NotifyType = NotifyType.DEFAULT, isCancel: Boolean = false) {
        checkPermission(context) {
            val notify = _progressNotifyBuilder.find { x -> x.notificationUUID == uuid } ?: return@checkPermission
            if (isCancel) {
                _progressNotifyBuilder.remove(notify)
            }
            updateNotification(context, notify, title, content, minValue, maxValue, notifyType, isCancel)
        }
    }
    private fun createNotificationChannel(context: Context, notifyType: NotifyType = NotifyType.DEFAULT) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(FConstants.NOTIFICATION_CHANNEL_ID, FConstants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            when (notifyType) {
                NotifyType.DEFAULT -> channel.enableVibration(false)
                NotifyType.WITH_SOUND -> {
                    channel.enableVibration(false)
                    channel.setSound(getSoundUri(context), getAudioAttribute())
                }
                NotifyType.WITH_VIBRATE -> {
                    channel.vibrationPattern = getVibratePattern()
                    channel.enableVibration(true)
                }
                NotifyType.WITH_S_V -> {
                    channel.vibrationPattern = getVibratePattern()
                    channel.setSound(getSoundUri(context), getAudioAttribute())
                    channel.enableVibration(true)
                }
            }
            _notificationManager.createNotificationChannel(channel)
        }
    }
    private fun sendNotification(context: Context, intent: Intent, title: String, content: String = "", notifyType: NotifyType = NotifyType.DEFAULT) {
        if (!checkPermission(context)) {
            return
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        _notificationManager.notify(notificationId++, notificationCompatBuild(context, title, content, pendingIntent, notifyType, true, false).build())
    }
    private fun sendNotification(context: Context, title: String, content: String = "", notifyType: NotifyType = NotifyType.DEFAULT) {
        if (!checkPermission(context)) {
            return
        }
        _notificationManager.notify(notificationId++, notificationCompatBuild(context, title, content, null, notifyType, true, false).build())
    }
    private fun indexToActivity(notifyIndex: NotifyIndex): Class<*> {
        return when (notifyIndex) {
            NotifyIndex.UNKNOWN -> MainActivity::class.java
            NotifyIndex.EDI_UPLOAD -> MainActivity::class.java
            NotifyIndex.EDI_FILE_UPLOAD -> EDIViewActivity::class.java
            NotifyIndex.EDI_FILE_REMOVE -> EDIViewActivity::class.java
            NotifyIndex.EDI_RESPONSE -> EDIViewActivity::class.java
            NotifyIndex.QNA_UPLOAD -> MainActivity::class.java
            NotifyIndex.QNA_FILE_UPLOAD -> MainActivity::class.java
            NotifyIndex.QNA_RESPONSE -> MainActivity::class.java
        }
    }
    private fun sendNotification(context: Context, notifyIndex: NotifyIndex, title: String, content: String? = null, notifyType: NotifyType = NotifyType.DEFAULT, isCancel: Boolean = false, thisPK: String = "") {
        if (!checkPermission(context)) {
            return
        }
        val intent = Intent(context, indexToActivity(notifyIndex)).apply {
            putExtras(Bundle().apply {
                putExtra("notifyIndex", notifyIndex.index)
                putExtra("thisPK", thisPK)
            })
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        _notificationManager.notify(notificationId++, notificationCompatBuild(context, title, content, pendingIntent, notifyType, true, !isCancel).build())
    }
    private fun sendNotification(context: Context, uuid: String, title: String, content: String? = null, minValue: Int = 0, maxValue: Int = 0, notifyType: NotifyType = NotifyType.DEFAULT, isCancel: Boolean = false) {
        if (!checkPermission(context)) {
            return
        }
        val deleteIntent = PendingIntent.getBroadcast(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)
        val notification = notificationCompatBuild(context, title, content, deleteIntent, notifyType, false, true)
            .setProgress(maxValue, minValue, maxValue == 0)
        val notificationId = this.notificationId++
        notification.setOngoing(!isCancel)
        _progressNotifyBuilder.add(ProgressNotifyModel(notification, uuid, notificationId))
        _notificationManager.notify(notificationId, notification.build())
    }
    private fun updateNotification(context: Context, progressNotifyModel: ProgressNotifyModel, title: String?, content: String = "", minValue: Int = 0, maxValue: Int = 0, notifyType: NotifyType = NotifyType.DEFAULT, isCancel: Boolean = false) {
        if (isCancel) {
            _notificationManager.cancel(progressNotifyModel.notificationId)
            return
        }
        if (!checkPermission(context)) {
            return
        }
        progressNotifyModel.notificationBuilder.apply {
            applyNotifyType(context, notifyType)
        }
        title?.let {
            progressNotifyModel.notificationBuilder.setContentTitle(it)
        }
        progressNotifyModel.notificationBuilder.setContentText(content)
        progressNotifyModel.notificationBuilder.setProgress(maxValue, minValue, maxValue == 0)
        _notificationManager.notify(progressNotifyModel.notificationId, progressNotifyModel.notificationBuilder.build())
    }

    private fun checkPermission(context: Context): Boolean {
        if ((context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.areNotificationsEnabled() == true) {
            return true
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun notificationCompatBuild(context: Context, title: String, content: String? = null, pendingIntent: PendingIntent?, notifyType: NotifyType, autoCancel: Boolean, onGoing: Boolean): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, FConstants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(autoCancel)
            .setOngoing(onGoing).apply {
                applyNotifyType(context, notifyType)
            }
    }
    private fun NotificationCompat.Builder.applyNotifyType(context: Context, notifyType: NotifyType) {
        when (notifyType) {
            NotifyType.DEFAULT -> setSilent(true)
            NotifyType.WITH_SOUND -> setSound(getSoundUri(context), AudioManager.STREAM_ALARM)
            NotifyType.WITH_VIBRATE -> {
                setSilent(true)
                setVibrate(getVibratePattern())
                setVibrate(context)
            }
            NotifyType.WITH_S_V -> {
                setSound(getSoundUri(context), AudioManager.STREAM_ALARM)
                setVibrate(getVibratePattern())
                setVibrate(context)
            }
        }
    }

    private fun getSoundUri(context: Context) = (ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.buff_music).toUri()
    private fun getAudioAttribute() = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .build()
    private fun setVibrate(context: Context) {
        val repeat = -1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.vibrate(
                CombinedVibration.createParallel(VibrationEffect.createWaveform(getVibratePattern(), getVibrateAmplitude(), repeat)))
        } else {
            @Suppress("DEPRECATION")
            (context.getSystemService(VIBRATOR_SERVICE) as? Vibrator)?.let { x ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    x.vibrate(VibrationEffect.createWaveform(getVibratePattern(), getVibrateAmplitude(), repeat))
                } else {
                    @Suppress("DEPRECATION")
                    x.vibrate(getVibratePattern(), repeat)
                }
            }
        }
    }
    private fun getVibratePattern() = longArrayOf(50, 150, 50, 150)
    private fun getVibrateAmplitude() = intArrayOf(50, 50, 50, 100)

    data class ProgressNotifyModel(
        var notificationBuilder: NotificationCompat.Builder,
        var notificationUUID: String,
        var notificationId: Int
    ) {
    }

    enum class NotifyType(var index: Int) {
        DEFAULT(1),
        WITH_SOUND(2),
        WITH_VIBRATE(3),
        WITH_S_V(4);
        companion object {
            fun fromIndex(index: Int?) = entries.firstOrNull { it.index == index } ?: DEFAULT
        }
    }
}
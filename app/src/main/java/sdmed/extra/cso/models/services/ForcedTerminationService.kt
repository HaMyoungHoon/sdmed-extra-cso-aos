package sdmed.extra.cso.models.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class ForcedTerminationService: Service() {
    override fun onBind(p0: Intent?) = null
    override fun onTaskRemoved(rootIntent: Intent?) {
        (application.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.cancelAll()
    }
}
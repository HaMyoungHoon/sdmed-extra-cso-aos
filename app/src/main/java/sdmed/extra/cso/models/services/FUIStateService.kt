package sdmed.extra.cso.models.services

import android.content.Context
import android.widget.Toast
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.dialog.loading.LoadingDialog

class FUIStateService {
    private var loadingDialog: LoadingDialog? = null
    fun toast(context: Context?, message: String?, duration: Int = Toast.LENGTH_SHORT) {
        if (context == null) {
            return
        }
        if (message.isNullOrBlank()) {
            return
        }
        val motherContext = context.applicationContext
        FCoroutineUtil.coroutineScope({
            Toast.makeText(motherContext, message, duration).show()
        })
    }
    fun loading(context: Context?, message: String = "", isVisible: Boolean = false) {
        if (!isVisible) {
            if (loadingDialog?.isShowing == true) {
                loadingDialog?.dismiss()
            }
            return
        }
        if (loadingDialog?.isShowing == true) {
            return
        }
        if (context == null) {
            return
        }
        loadingDialog = LoadingDialog(context, message)
        try {
            loadingDialog?.show()
        } catch (_: Exception) {
        }
    }
}
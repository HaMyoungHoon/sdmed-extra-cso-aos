package sdmed.extra.cso.views.landing

import android.content.Intent
import sdmed.extra.cso.MainActivity
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.databinding.LandingActivityBinding
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FVersionControl
import sdmed.extra.cso.views.dialog.message.MessageDialog
import sdmed.extra.cso.views.dialog.message.MessageDialogVM
import androidx.core.net.toUri
import sdmed.extra.cso.views.login.LoginActivity

class LandingActivity: FBaseActivity<LandingActivityBinding, LandingActivityVM>() {
    override var layoutId = R.layout.landing_activity
    override val dataContext: LandingActivityVM by lazy {
        LandingActivityVM(multiDexApplication)
    }

    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
        if (intent.getBooleanExtra("expired", false)) {
            toast(R.string.expired_token)
        } else if (intent.getBooleanExtra("logout", false)) {
            toast(R.string.do_logout)
        }

        loading()
        versionCheck()
    }
    private fun versionCheck() {
        dataContext.versionCheck {
            setEventListener()
            loading(false)
            if (!it.data.isNullOrEmpty()) {
                val asyncEvent = dataContext.relayCommand.asyncEvent ?: return@versionCheck
                val versionModel = it.data?.firstOrNull { it.able } ?: return@versionCheck
                val currentVersion = FMainApplication.ins.getVersionNameString()
                val check = FVersionControl.checkVersion(versionModel, currentVersion)
                if (check == FVersionControl.VersionResultType.NEED_UPDATE) {
                    MessageDialog(asyncEvent,
                        getString(R.string.new_version_update_desc),
                        getString(R.string.update_desc),
                        "",
                        false).show(supportFragmentManager, "")
                }
                page()
            }
        }
    }
    private fun page() {
        if (FRetrofitVariable.token.isNullOrBlank()) {
            dataContext.startVisible.value = true
            return
        }
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
    override fun setEventListener() {
        dataContext.addEventListener(object: IAsyncEventListener {
            override suspend fun onEvent(data: Any?) {
                setLayoutCommand(data)
                setMessageDialogCommand(data)
            }
        })
    }
    override fun setLayoutCommand(data: Any?) {
        val eventName = data as? LandingActivityVM.ClickEvent ?: return
        if (eventName == LandingActivityVM.ClickEvent.START) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun setMessageDialogCommand(data: Any?) {
        val eventName = data as? MessageDialogVM.ClickEvent ?: return
        when (eventName) {
            MessageDialogVM.ClickEvent.RIGHT -> startActivity(Intent(Intent.ACTION_VIEW).apply {
                this.data = "market://details?id=$packageName".toUri()
            })
            MessageDialogVM.ClickEvent.LEFT -> page()
        }
    }
}
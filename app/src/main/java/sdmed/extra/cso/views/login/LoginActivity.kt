package sdmed.extra.cso.views.login

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.MainActivity
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FMainApplication
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.databinding.LoginActivityBinding
import sdmed.extra.cso.models.eventbus.MultiSignEvent
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.dialog.bottomLogin.BottomLoginDialog

class LoginActivity: FBaseActivity<LoginActivityBinding, LoginActivityVM>() {
    override var layoutId = R.layout.login_activity
    override val dataContext: LoginActivityVM by lazy {
        LoginActivityVM(multiDexApplication)
    }
    override fun viewInit() {
        super.viewInit()
        observeText()
        FStorage.getMultiLoginData(this)?.let {
            dataContext.multiSignItems.value = it.toMutableList()
        }
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setBottomSelectCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? LoginActivityVM.ClickEvent ?: return
        when (eventName) {
            LoginActivityVM.ClickEvent.FORGOT_ID -> forgotIDEvent()
            LoginActivityVM.ClickEvent.FORGOT_PW -> forgotPWEvent()
            LoginActivityVM.ClickEvent.SIGN_IN -> signInEvent()
            LoginActivityVM.ClickEvent.MULTI_LOGIN -> multiLoginOn()
        }
    }
    private fun setBottomSelectCommand(data: Any?) {

    }
    private fun observeText() {
        lifecycleScope.launch {
            val ret1 = async { dataContext.loginID.collectLatest { fillDataCheck() } }
            val ret2 = async { dataContext.loginPW.collectLatest { fillDataCheck() } }
            ret1.await()
            ret2.await()
        }
    }
    private fun fillDataCheck() {
        dataContext.fillDataState.value = dataContext.loginID.value.length >= 3 && dataContext.loginPW.value.length >= 4
    }

    private fun forgotIDEvent() {
        // fragment on? dialog over?
    }
    private fun forgotPWEvent() {
        // fragment on? dialog over?
    }
    private fun signInEvent() {
        if (!dataContext.fillDataState.value) {
            return
        }
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.signIn()
            loading(false)
            if (ret.result == true) {
                FRetrofitVariable.token = ret.data
                FStorage.setAuthToken(this@LoginActivity, ret.data)
                addLoginData()
                finish()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                return@coroutineScope
            }
            toast(ret.msg)
        })
    }
    private fun multiLoginOn() {
        BottomLoginDialog(false, dataContext.relayCommand).show(supportFragmentManager, "")
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun multiSignEvent(data: MultiSignEvent) {
        finish()
        if (!FMainApplication.isMainActivityRunning) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
    }
}
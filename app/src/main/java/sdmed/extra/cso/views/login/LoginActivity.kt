package sdmed.extra.cso.views.login

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.MainActivity
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.databinding.LoginActivityBinding
import sdmed.extra.cso.models.retrofit.FRetrofitVariable

class LoginActivity: FBaseActivity<LoginActivityBinding, LoginActivityVM>() {
    override var layoutId = R.layout.login_activity
    override val dataContext: LoginActivityVM by lazy {
        LoginActivityVM(multiDexApplication)
    }
    override fun viewInit() {
        binding?.dataContext = dataContext
        super.viewInit()
        observeText()
    }
    override fun setLayoutCommand(data: Any?) {
        val eventName = data as? LoginActivityVM.ClickEvent ?: return
        when (eventName) {
            LoginActivityVM.ClickEvent.FORGOT_ID -> forgotIDEvent()
            LoginActivityVM.ClickEvent.FORGOT_PW -> forgotPWEvent()
            LoginActivityVM.ClickEvent.SIGN_IN -> signInEvent()
        }
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
        dataContext.signIn { x ->
            loading(false)
            if (x.result == true && x.data != null) {
                FRetrofitVariable.token = x.data
                FStorage.setAuthToken(this, x.data)
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                toast(x.msg)
            }
        }
    }
}
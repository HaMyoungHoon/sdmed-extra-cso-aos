package sdmed.extra.cso.views.login

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.databinding.PasswordChangeActivityBinding
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions

class PasswordChangeActivity: FBaseActivity<PasswordChangeActivityBinding, PasswordChangeActivityVM>() {
    override var layoutId = R.layout.password_change_activity
    override val dataContext: PasswordChangeActivityVM by lazy {
        PasswordChangeActivityVM(multiDexApplication)
    }

    override fun viewInit() {
        super.viewInit()
        observeText()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? PasswordChangeActivityVM.ClickEvent ?: return
        when (eventName) {
            PasswordChangeActivityVM.ClickEvent.CLOSE -> finish()
            PasswordChangeActivityVM.ClickEvent.CHANGE -> change()
        }
    }
    private fun change() {
        if (dataContext.changeAble.value == false) {
            return
        }

        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.putPasswordChange()
            loading(false)
            if (ret.result == true) {
                finish()
                return@coroutineScope
            }
            toast(ret.msg)
        })
    }
    private fun observeText() {
        lifecycleScope.launch {
            val ret1 = async { dataContext.currentPW.collectLatest { changeCheck() } }
            val ret2 = async { dataContext.afterPW.collectLatest { afterPWCheck() } }
            val ret3 = async { dataContext.confirmPW.collectLatest { afterPWCheck() } }
            ret1.await()
            ret2.await()
            ret3.await()
        }
    }
    private fun changeCheck() {
        if (dataContext.afterPWRuleVisible.value != dataContext.confirmPWRuleVisible.value) {
            dataContext.pwUnMatchVisible.value = true
            return
        }
        dataContext.pwUnMatchVisible.value = false
        if (dataContext.currentPW.value.isBlank() || dataContext.afterPW.value.isBlank() || dataContext.confirmPW.value.isBlank()) {
            dataContext.changeAble.value = false
            return
        }

        dataContext.changeAble.value = true
    }
    private fun afterPWCheck() {
        if (FExtensions.regexPasswordCheck(dataContext.afterPW.value) != true) {
            dataContext.afterPWRuleVisible.value = true
            return
        }
        dataContext.afterPWRuleVisible.value = false
        if (FExtensions.regexPasswordCheck(dataContext.confirmPW.value) != true) {
            dataContext.confirmPWRuleVisible.value = true
            return
        }
        dataContext.confirmPWRuleVisible.value = false
        changeCheck()
    }
}
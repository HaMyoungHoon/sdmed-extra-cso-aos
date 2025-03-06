package sdmed.extra.cso.bases

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.multidex.MultiDexApplication
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import sdmed.extra.cso.interfaces.command.IAsyncEventListener
import sdmed.extra.cso.models.eventbus.TokenCheckEvent
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRole.Companion.getFlag
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.models.retrofit.users.UserStatus
import sdmed.extra.cso.models.services.FUIStateService
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.views.landing.LandingActivity
import sdmed.extra.cso.views.login.LoginActivity

abstract class FBaseActivity<T1: ViewDataBinding, T2: FBaseViewModel>(val needRoles: UserRoles = UserRole.None.toS()): AppCompatActivity(), KodeinAware {
    protected abstract var layoutId: Int
    protected var binding: T1? = null
    protected abstract val dataContext: T2
    protected val uiStateService: FUIStateService by lazy {
        FUIStateService()
    }
    val multiDexApplication: MultiDexApplication by lazy {
        this.application as MultiDexApplication
    }
    override val kodein: Kodein by closestKodein()

    private var _needTokenRefresh = true
    val initAble get() = !_needTokenRefresh
    var isActivityPause = false
        private set
    protected var myState: UserStatus = UserStatus.None
        private set
    protected var haveRole: Boolean = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindingBuff: T1 = DataBindingUtil.setContentView(this, layoutId)
        bindingBuff.lifecycleOwner = this
        val method = bindingBuff::class.java.getMethod("setDataContext", dataContext::class.java)
        method.invoke(bindingBuff, dataContext)
        binding = bindingBuff
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        _needTokenRefresh = getToken()
        if (initAble) {
            FCoroutineUtil.coroutineScope({
                afterOnCreate()
            })
        }
    }
    override fun onResume() {
        isActivityPause = false
        super.onResume()
        if (!_needTokenRefresh) {
            _needTokenRefresh = getToken()
        }
    }
    override fun onPause() {
        isActivityPause = true
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        loading(false)
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        binding = null
    }

    private suspend fun afterOnCreate() {
        loading()
        if (this !is LandingActivity && this !is LoginActivity) {
            stateCheck()
            if (myState != UserStatus.Live) {
                loading(false)
                return
            }
            roleCheck()
            if (!haveRole) {
                loading(false)
                return
            }
        }
        loading(false)

        viewInit()
    }
    open fun viewInit() {
        setEventListener()
    }
    open fun setEventListener() {
        dataContext.addEventListener(object: IAsyncEventListener {
            override suspend fun onEvent(data: Any?) {
                setLayoutCommand(data)
            }
        })
    }
    open fun setLayoutCommand(data: Any?) {
    }

    protected fun toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) = toast(resources.getString(resId), duration)
    protected fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) = uiStateService.toast(this, message, duration)
    protected fun loading(message: String = "", isVisible: Boolean = true, alpha: Float = 0F) = uiStateService.loading(this, message, isVisible, alpha)
    protected fun loading(isVisible: Boolean = true, alpha: Float = 0F) = uiStateService.loading(this, "", isVisible, alpha)

    private suspend fun stateCheck() {
        val ret = dataContext.getMyState()
        if (ret.result == true) {
            myState = ret.data ?: UserStatus.None
        } else {
            toast(ret.msg)
        }
    }
    private suspend fun roleCheck() {
        if (needRoles.getFlag() == 0) {
            haveRole = true
            return
        }
        val ret = dataContext.getMyRole()
        if (ret.result == true) {
            haveRole = ((ret.data ?: 0) and needRoles.getFlag()) != 0
        } else {
            haveRole = false
            toast(ret.msg)
        }
    }
    private fun getToken(): Boolean {
        if (FRetrofitVariable.token == null) {
            FRetrofitVariable.token = FStorage.getAuthToken(this)
        }
        if (FRetrofitVariable.token.isNullOrBlank()) {
            goToLogin()
            return true
        }
        try {
            if (!FAmhohwa.checkInvalidToken(this)) {
                if (FStorage.getRefreshing(this)) {
                    return true
                }
                FStorage.setRefreshing(this, true)
                tokenRefresh()
                return true
            }
            return false
        } catch (_: Exception) {
            goToLogin()
        }
        return true
    }
    private fun goToLogin(expired: Boolean = false) {
        if (this is LandingActivity || this is LoginActivity) {
            FCoroutineUtil.coroutineScope({
                afterOnCreate()
            })
        } else {
            FAmhohwa.logout(this, expired = expired)
        }
    }
    private fun reCreate() {
        if (this is LandingActivity) {
            return
        } else {
            recreate()
        }
    }
    private fun tokenRefresh() {
        if (FRetrofitVariable.token.isNullOrBlank()) {
            goToLogin(true)
            return
        }
        dataContext.tokenRefresh { x ->
            FStorage.setRefreshing(this, false)
            if (x.result == true) {
                val newToken = x.data ?: ""
                if (FAmhohwa.rhsTokenIsMost(newToken)) {
                    FStorage.setAuthToken(this, newToken)
                    FStorage.addMultiLoginToken(this, newToken)
                }
                FRetrofitVariable.token = FStorage.getAuthToken(this)
            } else {
                FStorage.delMultiLoginToken(this, FStorage.getAuthToken(this))
                FStorage.removeAuthToken(this)
                goToLogin(true)
            }
            FCoroutineUtil.coroutineScope({
                afterOnCreate()
            })
        }
    }

    protected fun shouldShowRequestPermissionRationale(permissions: Array<String>) = permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }
    protected fun hasPermissionsGranted(permissions: Array<String>) = permissions.none {
        ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun tokenCheckEvent(event: TokenCheckEvent) {
    }
}
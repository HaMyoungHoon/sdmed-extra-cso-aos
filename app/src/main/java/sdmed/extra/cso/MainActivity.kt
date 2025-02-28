package sdmed.extra.cso

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.MainActivityBinding
import sdmed.extra.cso.models.common.NotifyIndex
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.main.edi.EDIFragment
import sdmed.extra.cso.views.main.edi.ediRequest.EDIRequestActivity
import sdmed.extra.cso.views.main.edi.ediView.EDIViewActivity
import sdmed.extra.cso.views.main.my.MyFragment
import sdmed.extra.cso.views.main.price.PriceFragment
import sdmed.extra.cso.views.main.qna.QnAFragment
import sdmed.extra.cso.views.main.qna.qnaView.QnAViewActivity

class MainActivity: FBaseActivity<MainActivityBinding, MainActivityVM>(UserRoles.of(UserRole.None)) {
    override var layoutId = R.layout.main_activity
    override val dataContext: MainActivityVM by lazy {
        MainActivityVM(multiDexApplication)
    }
    var finishConfirmed = false
    private var _backPressed: OnBackPressedCallback? = null

    override fun onDestroy() {
        _backPressed = null
        super.onDestroy()
    }
    override fun viewInit() {
        super.viewInit()
        setBackPressed()
        openPage()
        mqttInit()
        notificationCheck()
    }
    override fun setLayoutCommand(data: Any?) {
        val eventName = data as? MainActivityVM.ClickEvent ?: return
        when (eventName) {
            MainActivityVM.ClickEvent.EDI -> openEDIFragment(eventName)
            MainActivityVM.ClickEvent.PRICE -> openPriceFragment(eventName)
            MainActivityVM.ClickEvent.HOME -> openHomeFragment(eventName)
            MainActivityVM.ClickEvent.QNA -> openQnAFragment(eventName)
            MainActivityVM.ClickEvent.MY -> openMyFragment(eventName)
        }
    }
    private fun openEDIFragment(eventName: MainActivityVM.ClickEvent) {
        allMenuUnSelect(eventName)
        replaceFragment(EDIFragment())
    }
    private fun openPriceFragment(eventName: MainActivityVM.ClickEvent) {
        allMenuUnSelect(eventName)
        replaceFragment(PriceFragment())
    }
    private fun openHomeFragment(eventName: MainActivityVM.ClickEvent) {
        startActivity(Intent(this, EDIRequestActivity::class.java))
//        allMenuUnSelect(eventName)
//        replaceFragment(HomeFragment())
    }
    private fun openQnAFragment(eventName: MainActivityVM.ClickEvent) {
        allMenuUnSelect(eventName)
        replaceFragment(QnAFragment())
    }
    private fun openMyFragment(eventName: MainActivityVM.ClickEvent) {
        allMenuUnSelect(eventName)
        replaceFragment(MyFragment())
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_fragment, fragment)
            .setReorderingAllowed(true)
            .commitAllowingStateLoss()
    }
    private fun allMenuUnSelect(eventName: MainActivityVM.ClickEvent) {
        dataContext.ediMenuState.value = eventName == MainActivityVM.ClickEvent.EDI
        dataContext.priceMenuState.value = eventName == MainActivityVM.ClickEvent.PRICE
        dataContext.qnaMenuState.value = eventName == MainActivityVM.ClickEvent.QNA
        dataContext.myMenuState.value = eventName == MainActivityVM.ClickEvent.MY
    }
    private fun setBackPressed() {
        _backPressed = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!finishConfirmed) {
                    toast(R.string.back_btn_close_desc)
                    finishConfirmed = true
                    Handler(Looper.getMainLooper()).postDelayed({ finishConfirmed = false }, 3000L)
                } else {
                    finishAffinity()
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, _backPressed!!)
    }
    private fun openPage() {
        val notifyIndex = NotifyIndex.parseIndex(intent.getIntExtra("notifyIndex", 0))
        val thisPK = intent.getStringExtra("thisPK") ?: ""
        when (notifyIndex) {
            NotifyIndex.UNKNOWN -> openEDIFragment(MainActivityVM.ClickEvent.EDI)
            NotifyIndex.EDI_UPLOAD -> openEDIFragment(MainActivityVM.ClickEvent.EDI)
            NotifyIndex.EDI_FILE_UPLOAD -> openEDIFragment(MainActivityVM.ClickEvent.EDI)
            NotifyIndex.EDI_FILE_REMOVE -> openEDIFragment(MainActivityVM.ClickEvent.EDI)
            NotifyIndex.EDI_RESPONSE -> openEDIFragment(MainActivityVM.ClickEvent.EDI)
            NotifyIndex.QNA_UPLOAD -> openQnAFragment(MainActivityVM.ClickEvent.QNA)
            NotifyIndex.QNA_FILE_UPLOAD -> openQnAFragment(MainActivityVM.ClickEvent.QNA)
            NotifyIndex.QNA_RESPONSE -> openQnAFragment(MainActivityVM.ClickEvent.QNA)
        }
        if (thisPK.isNotEmpty()) {
            when (notifyIndex) {
                NotifyIndex.EDI_UPLOAD -> startActivity(Intent(this, EDIViewActivity::class.java).apply { putExtra("thisPK", thisPK) })
                NotifyIndex.EDI_FILE_UPLOAD -> startActivity(Intent(this, EDIViewActivity::class.java).apply { putExtra("thisPK", thisPK) })
                NotifyIndex.EDI_FILE_REMOVE -> startActivity(Intent(this, EDIViewActivity::class.java).apply { putExtra("thisPK", thisPK) })
                NotifyIndex.EDI_RESPONSE -> startActivity(Intent(this, EDIViewActivity::class.java).apply { putExtra("thisPK", thisPK) })
                NotifyIndex.QNA_UPLOAD -> startActivity(Intent(this, QnAViewActivity::class.java).apply { putExtra("thisPK", thisPK) })
                NotifyIndex.QNA_FILE_UPLOAD -> startActivity(Intent(this, QnAViewActivity::class.java).apply { putExtra("thisPK", thisPK) })
                NotifyIndex.QNA_RESPONSE -> startActivity(Intent(this, QnAViewActivity::class.java).apply { putExtra("thisPK", thisPK) })
                else -> { }
            }
        }
    }
    private fun mqttInit() {
        FCoroutineUtil.coroutineScope({
            dataContext.mqttService.mqttInit()
        })
    }
    private fun notificationCheck() {
        if (!hasNotificationGranted()) {
            requestNotificationGranted()
        }
    }
    private fun hasNotificationGranted(): Boolean {
        return hasPermissionsGranted(FConstants.NOTIFICATION_PERMISSION)
    }
    private fun requestNotificationGranted() {
        requestPermissions(FConstants.NOTIFICATION_PERMISSION, FConstants.Permit.LOCATION.index)
    }
}
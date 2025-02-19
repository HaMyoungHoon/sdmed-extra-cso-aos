package sdmed.extra.cso

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.databinding.MainActivityBinding
import sdmed.extra.cso.models.mqtt.MqttContentModel
import sdmed.extra.cso.models.mqtt.MqttContentType
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.views.main.edi.EDIFragment
import sdmed.extra.cso.views.main.home.HomeFragment
import sdmed.extra.cso.views.main.my.MyFragment
import sdmed.extra.cso.views.main.price.PriceFragment
import sdmed.extra.cso.views.main.qna.QnAFragment

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
        binding?.dataContext = dataContext
        super.viewInit()
        setBackPressed()
        openEDIFragment(MainActivityVM.ClickEvent.EDI)
        mqttInit()
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
        allMenuUnSelect(eventName)
        replaceFragment(HomeFragment())
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
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, _backPressed!!)
    }


    private fun mqttInit() {
        FCoroutineUtil.coroutineScope({
            dataContext.mqttService.mqttInit()
        })
    }
    private fun test() {
        val topic = "aos-extra-cso" // "private/fc1b9a2e-ae8a-437d-8074-a19d3acd1813"
        val mqttContentModel = MqttContentModel().apply {
            this.senderName = "ㅎㅁㅎ"
            this.content = "안드로이드 테스트"
            this.contentType = MqttContentType.None
            this.targetItemPK = "1234"
        }
        FCoroutineUtil.coroutineScope({
            dataContext.mqttService.mqttSend(topic, mqttContentModel)
        })
    }
}
package sdmed.extra.cso.views.main.my

import android.content.Intent
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.MyFragmentBinding
import sdmed.extra.cso.models.eventbus.MultiSignEvent
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.views.dialog.bottomLogin.BottomLoginDialog
import sdmed.extra.cso.views.dialog.bottomLogin.BottomLoginDialogVM
import sdmed.extra.cso.views.login.LoginActivity
import sdmed.extra.cso.views.login.PasswordChangeActivity
import java.util.ArrayList

class MyFragment: FBaseFragment<MyFragmentBinding, MyFragmentVM>() {
    override var layoutId = R.layout.my_fragment
    override val dataContext: MyFragmentVM by lazy {
        MyFragmentVM(multiDexApplication)
    }
    override fun viewInit() {
        super.viewInit()
        setHospitalAdapter()
        setPharmaAdapter()
        observeData()
        getData()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setHospitalCommand(data)
        setBottomLoginCommand(data)
    }

    private fun getData() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getData()
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
            }
        })
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? MyFragmentVM.ClickEvent ?: return
        when (eventName) {
            MyFragmentVM.ClickEvent.LOGOUT -> logout()
            MyFragmentVM.ClickEvent.PASSWORD_CHANGE -> startActivity(Intent(contextBuff, PasswordChangeActivity::class.java))
            MyFragmentVM.ClickEvent.MULTI_LOGIN -> bottomLoginOn()
            MyFragmentVM.ClickEvent.IMAGE_TAXPAYER -> { }
            MyFragmentVM.ClickEvent.IMAGE_BANK_ACCOUNT -> { }
            MyFragmentVM.ClickEvent.IMAGE_CSO_REPORT -> { }
            MyFragmentVM.ClickEvent.IMAGE_MARKETING_CONTRACT -> { }
        }
    }
    private fun setHospitalCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? HospitalModel.ClickEvent ?: return
        val dataBuff = data[1] as? HospitalModel ?: return
        when (eventName) {
            HospitalModel.ClickEvent.THIS -> {
                dataContext.selectedHos.value = dataBuff
            }
        }
    }
    private fun setBottomLoginCommand(data: Any?) {
        val eventName = data as? BottomLoginDialogVM.ClickEvent ?: return
        when (eventName) {
            BottomLoginDialogVM.ClickEvent.CLOSE -> { }
            BottomLoginDialogVM.ClickEvent.ADD -> startActivity(Intent(contextBuff, LoginActivity::class.java))
        }
    }
    private fun setHospitalAdapter() = UserHospitalAdapter(dataContext.relayCommand).also { binding?.rvUserHospital?.adapter = it }
    private fun setPharmaAdapter() = UserPharmaAdapter(dataContext.relayCommand).also { binding?.rvUserPharma?.adapter = it }
    private fun observeData() {
        lifecycleScope.launch {
            dataContext.selectedHos.collectLatest {
                dataContext.pharmaList.value = it.pharmaList
            }
        }
        lifecycleScope.launch {
            dataContext.thisData.collectLatest {
                dataContext.hosList.value = it.hosList
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }
    private fun logout() {
        val context = contextBuff ?: return
        FAmhohwa.logout(context)
        FStorage.logoutMultiLoginData(context)
    }
    private fun bottomLoginOn() {
        BottomLoginDialog(true, dataContext.relayCommand).show(childFragmentManager, "")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun multiSignEvent(data: MultiSignEvent) {
        getData()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerUserHospitalList")
        fun setRecyclerUserHospitalList(recyclerView: RecyclerView, listItem: StateFlow<MutableList<HospitalModel>>?) {
            val adapter = recyclerView.adapter as? UserHospitalAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItem?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
        @JvmStatic
        @BindingAdapter("recyclerUserPharmaList")
        fun setRecyclerUserPharmaList(recyclerView: RecyclerView, listItem: StateFlow<MutableList<PharmaModel>>?) {
            val adapter = recyclerView.adapter as? UserPharmaAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItem?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}
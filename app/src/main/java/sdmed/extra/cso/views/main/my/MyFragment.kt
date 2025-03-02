package sdmed.extra.cso.views.main.my

import android.content.Intent
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.databinding.MyFragmentBinding
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
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
            MyFragmentVM.ClickEvent.LOGOUT -> FAmhohwa.logout(contextBuff)
            MyFragmentVM.ClickEvent.PASSWORD_CHANGE -> startActivity(Intent(contextBuff, PasswordChangeActivity::class.java))
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
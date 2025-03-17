package sdmed.extra.cso.views.main.my

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.MyFragmentBinding
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.eventbus.MultiSignEvent
import sdmed.extra.cso.models.eventbus.UserFileUploadEvent
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.hospitals.HospitalModel
import sdmed.extra.cso.models.retrofit.pharmas.PharmaModel
import sdmed.extra.cso.models.retrofit.users.UserFileType
import sdmed.extra.cso.utils.FAmhohwa
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FStorage
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.FStorage.putParcelable
import sdmed.extra.cso.utils.FStorage.putParcelableList
import sdmed.extra.cso.views.dialog.bottomLogin.BottomLoginDialog
import sdmed.extra.cso.views.dialog.bottomLogin.BottomLoginDialogVM
import sdmed.extra.cso.views.login.LoginActivity
import sdmed.extra.cso.views.login.PasswordChangeActivity
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.media.view.MediaViewActivity
import java.util.ArrayList

class MyFragment: FBaseFragment<MyFragmentBinding, MyFragmentVM>() {
    override var layoutId = R.layout.my_fragment
    override val dataContext: MyFragmentVM by lazy {
        MyFragmentVM(multiDexApplication)
    }
    private var _imagePickerResult: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityResult()
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

    private fun registerActivityResult() {
        _imagePickerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            val mediaTypeIndex = it.data?.getStringExtra(FConstants.MEDIA_TARGET_PK)?.toIntOrNull() ?: return@registerForActivityResult
            val mediaList = it.data?.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST) ?: return@registerForActivityResult
            if (mediaTypeIndex == -1) return@registerForActivityResult
            if (mediaList.isEmpty()) return@registerForActivityResult
            loading()
            dataContext.userFileUpload(mediaTypeIndex, mediaList)
        }
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
            MyFragmentVM.ClickEvent.IMAGE_TAXPAYER -> taxImageClick()
            MyFragmentVM.ClickEvent.IMAGE_BANK_ACCOUNT -> bankAccountImageClick()
            MyFragmentVM.ClickEvent.IMAGE_CSO_REPORT -> csoReportImageClick()
            MyFragmentVM.ClickEvent.IMAGE_MARKETING_CONTRACT -> marketingContractImageClick()
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
    private fun taxImageClick() {
        val context = contextBuff ?: return
        val blobUrl = dataContext.thisData.value.taxPayerUrl
        if (blobUrl != null) {
            startActivity((Intent(context, MediaViewActivity::class.java).apply {
                putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
                    this.blobUrl = blobUrl
                    this.mimeType = dataContext.thisData.value.taxPayerMimeType ?: ""
                })
            }))
            return
        }
        _imagePickerResult?.launch(Intent(contextBuff, MediaPickerActivity::class.java).apply {
            putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.Taxpayer.index.toString())
            putExtra(FConstants.MEDIA_MAX_COUNT, 1)
        })
    }
    private fun bankAccountImageClick() {
        val context = contextBuff ?: return
        val blobUrl = dataContext.thisData.value.bankAccountUrl
        if (blobUrl != null) {
            startActivity((Intent(context, MediaViewActivity::class.java).apply {
                putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
                    this.blobUrl = blobUrl
                    this.mimeType = dataContext.thisData.value.bankAccountMimeType ?: ""
                })
            }))
            return
        }
        _imagePickerResult?.launch(Intent(contextBuff, MediaPickerActivity::class.java).apply {
            putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.BankAccount.index.toString())
            putExtra(FConstants.MEDIA_MAX_COUNT, 1)
        })
    }
    private fun csoReportImageClick() {
        val context = contextBuff ?: return
        val blobUrl = dataContext.thisData.value.csoReportUrl
        if (blobUrl != null) {
            startActivity((Intent(context, MediaViewActivity::class.java).apply {
                putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
                    this.blobUrl = blobUrl
                    this.mimeType = dataContext.thisData.value.csoReportMimeType ?: ""
                })
            }))
            return
        }
        _imagePickerResult?.launch(Intent(contextBuff, MediaPickerActivity::class.java).apply {
            putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.CsoReport.index.toString())
            putExtra(FConstants.MEDIA_MAX_COUNT, 1)
        })
    }
    private fun marketingContractImageClick() {
        val context = contextBuff ?: return
        val blobUrl = dataContext.thisData.value.marketingContractUrl
        if (blobUrl != null) {
            startActivity((Intent(context, MediaViewActivity::class.java).apply {
                putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().apply {
                    this.blobUrl = blobUrl
                    this.mimeType = dataContext.thisData.value.marketingContractMimeType ?: ""
                })
            }))
            return
        }
        _imagePickerResult?.launch(Intent(contextBuff, MediaPickerActivity::class.java).apply {
            putExtra(FConstants.MEDIA_TARGET_PK, UserFileType.MarketingContract.index.toString())
            putExtra(FConstants.MEDIA_MAX_COUNT, 1)
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun multiSignEvent(data: MultiSignEvent) {
        getData()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun userFileUploadEvent(data: UserFileUploadEvent) {
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
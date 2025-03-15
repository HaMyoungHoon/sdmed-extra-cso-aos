package sdmed.extra.cso.views.main.edi.ediRequest

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseFragment
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.EdiRequestNewFragmentBinding
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.SelectListModel
import sdmed.extra.cso.models.eventbus.EDIUploadEvent
import sdmed.extra.cso.models.retrofit.edi.EDIApplyDateModel
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIType
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.utils.FCameraUtil
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import sdmed.extra.cso.utils.FStorage.getParcelable
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.FStorage.putParcelableList
import sdmed.extra.cso.views.dialog.select.SelectDialog
import sdmed.extra.cso.views.hospitalMap.hospitalFind.HospitalFindActivity
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import java.io.File
import java.util.ArrayList

class EDIRequestNewFragment: FBaseFragment<EdiRequestNewFragmentBinding, EDIRequestNewFragmentVM>(UserRoles.of(UserRole.Admin, UserRole.CsoAdmin, UserRole.BusinessMan)) {
    override var layoutId = R.layout.edi_request_new_fragment
    override val dataContext: EDIRequestNewFragmentVM by lazy {
        EDIRequestNewFragmentVM(multiDexApplication)
    }
    private var _cameraUtil: FCameraUtil? = null
    private var _cameraResult: ActivityResultLauncher<Intent>? = null
    private var _imagePickerResult: ActivityResultLauncher<Intent>? = null
    private var _externalManageResult: ActivityResultLauncher<Intent>? = null
    private var _hospitalFindResult: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityResult()
    }
    override fun viewInit() {
        super.viewInit()
        setApplyDateAdapter()
        setEDIPharmaFileCombinedAdapter()
        getEdiTypeList()
        observeText()
        getData()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setApplyDateCommand(data)
        setPharmaFileCombinedCommand(data)
        setUploadBuffCommand(data)
        setSelectDialogCommand(data)
    }
    override fun onDestroy() {
        _cameraUtil = null
        _cameraResult?.unregister()
        _cameraResult = null
        _imagePickerResult?.unregister()
        _imagePickerResult = null
        _externalManageResult?.unregister()
        _externalManageResult = null
        _hospitalFindResult?.unregister()
        _hospitalFindResult = null
        super.onDestroy()
    }

    private fun registerActivityResult() {
        _cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            val context = contextBuff ?: return@registerForActivityResult
            val pharmaBuffPK = _cameraUtil?.targetPK ?: return@registerForActivityResult
            val cameraPath = _cameraUtil?.path ?: return@registerForActivityResult
            val imageNames = "edi_shot${FExtensions.getTodayString()}.jpeg"
            val imagePath = FImageUtils.uriCopyToTempFolder(context, File(cameraPath), imageNames).toString()
            val fileType: MediaFileType = MediaFileType.IMAGE
            dataContext.addImage(pharmaBuffPK, imagePath, imageNames, fileType, FContentsType.type_jpeg)
            savableCheck()
        }
        _imagePickerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            val pharmaBuffPK = it.data?.getStringExtra(FConstants.MEDIA_TARGET_PK) ?: ""
            val mediaList = it.data?.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST)
            dataContext.reSetImage(pharmaBuffPK, mediaList)
            savableCheck()
        }
        _externalManageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {

                    } else {

                    }
                }
            }
        }
        _hospitalFindResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            val hospitalTempModel = it.data?.getParcelable<HospitalTempModel>(FConstants.HOSPITAL_TEMP) ?: return@registerForActivityResult
            dataContext.setHospitalTemp(hospitalTempModel)
        }
    }
    private fun setApplyDateAdapter() = EDIRequestApplyDateAdapter(dataContext.relayCommand).also { binding?.rvApplyDate?.adapter = it }
    private fun setEDIPharmaFileCombinedAdapter() = EDIPharmaFileCombinedAdapter(dataContext.relayCommand).also { binding?.rvPharmaFileCombined?.adapter = it }

    private fun setThisCommand(data: Any?) {
        val eventName = data as? EDIRequestNewFragmentVM.ClickEvent ?: return
        when (eventName) {
            EDIRequestNewFragmentVM.ClickEvent.SAVE -> save()
            EDIRequestNewFragmentVM.ClickEvent.HOSPITAL_FIND -> hospitalFind()
        }
    }
    private fun setApplyDateCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIApplyDateModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIApplyDateModel ?: return
        when (eventName) {
            EDIApplyDateModel.ClickEvent.THIS -> {
                dataContext.applyDateSelect(dataBuff)
            }
        }
    }
    private fun setPharmaFileCombinedCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIPharmaBuffModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIPharmaBuffModel ?: return
        when (eventName) {
            EDIPharmaBuffModel.ClickEvent.THIS -> { }
            EDIPharmaBuffModel.ClickEvent.OPEN -> {
                dataBuff.isOpen.value = !dataBuff.isOpen.value
            }
            EDIPharmaBuffModel.ClickEvent.ADD -> addImage(dataBuff)
        }
    }
    private fun setUploadBuffCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? MediaPickerSourceModel.ClickEvent ?: return
        val dataBuff = data[1] as? MediaPickerSourceModel ?: return
        when (eventName) {
            MediaPickerSourceModel.ClickEvent.SELECT ->  {
                dataContext.delImage(dataBuff.thisPK)
                savableCheck()
            }
            else -> { }
        }
    }
    private fun setSelectDialogCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? SelectListModel.ClickEvent ?: return
        val dataBuff = data[1] as? SelectListModel ?: return
        when (eventName) {
            SelectListModel.ClickEvent.SELECT -> {
                if (dataBuff.itemIndex == 0) {
                    if (hasCameraGranted()) {
                        cameraOpen(dataBuff.data)
                    } else {
                        requestCameraPermissions()
                    }
                } else if (dataBuff.itemIndex == 1) {
                    if (hasReadExternalGranted()) {
                        imagePickerOpen(dataBuff.data)
                    } else {
                        requestReadExternalPermissions()
                    }
                }
            }
        }
    }
    private fun cameraOpen(data: Any?) {
        val cameraResult = _cameraResult ?: return
        val context = contextBuff ?: return
        val pharmaBuffPK = (data as? EDIPharmaBuffModel)?.thisPK ?: ""
        _cameraUtil = FCameraUtil(context, cameraResult).apply { showCamera(pharmaBuffPK) }
    }
    private fun imagePickerOpen(data: Any?) {
        _imagePickerResult?.launch(Intent(contextBuff, MediaPickerActivity::class.java).apply {
            (data as? EDIPharmaBuffModel)?.let {
                putExtra(FConstants.MEDIA_TARGET_PK, it.thisPK)
                putParcelableList(FConstants.MEDIA_LIST, ArrayList(it.uploadItems.value))
            }
        })
    }
    private fun getData() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getData()
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
                return@coroutineScope
            }
            getPharmaList()
        })
    }
    private fun getPharmaList() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getPharmaList()
            loading(false)
            if (ret.result != true) {
                toast(ret.msg)
                return@coroutineScope
            }
        })
    }
    private fun getEdiTypeList() {
        dataContext.ediTypeModel.value = EDIType.allEDITypeList()
    }
    private fun observeText() {
        lifecycleScope.launch {
            val ret1 = async { dataContext.tempOrgName.collectLatest { savableCheck() } }
            val ret2 = async { dataContext.searchString.collectLatest { filterItem() } }
            ret1.await()
            ret2.await()
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }
    private fun savableCheck() {
        dataContext.savableCheck()
    }
    private fun filterItem() {
        dataContext.filterItem()
    }
    private fun addImage(data: EDIPharmaBuffModel) {
        dataContext.isSavable.value = false
        val items = mutableListOf<SelectListModel>()
        items.add(SelectListModel().apply {
            itemIndex = 0
            iconResId = R.drawable.vector_camera_def
            stringResId = R.string.camera_desc
            this.data = data
        })
        items.add(SelectListModel().apply {
            itemIndex = 1
            iconResId = R.drawable.vector_file_def
            stringResId = R.string.file_desc
            this.data = data
        })
        SelectDialog(items, 0, dataContext.relayCommand).show(childFragmentManager, "")
    }
    private fun save() {
        if (dataContext.pharmaModel.value.none { x -> x.uploadItems.value.isNotEmpty() }) {
            return
        }
        if (!dataContext.isSavable.value) {
            return
        }
        dataContext.selectApplyDate ?: return
        if (dataContext.tempOrgName.value.isBlank()) {
            return
        }

        toast(R.string.edi_file_upload)
        loading()
        dataContext.startBackgroundService()
    }
    private fun hospitalFind() {
        val context = contextBuff ?: return
        _hospitalFindResult?.launch(Intent(context, HospitalFindActivity::class.java))
    }
    private fun hasCameraGranted(): Boolean {
        return hasPermissionsGranted(FConstants.CAMERA_PERMISSION)
    }
    private fun hasReadExternalGranted(): Boolean {
        val permissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            FConstants.READ_EXTERNAL_PERMISSION_32
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            FConstants.READ_EXTERNAL_PERMISSION_33
        } else {
            FConstants.READ_EXTERNAL_PERMISSION_34
        }

        return hasPermissionsGranted(permissions)
    }
    private fun requestCameraPermissions() {
        activity?.requestPermissions(FConstants.CAMERA_PERMISSION, FConstants.Permit.CAMERA.index)
    }
    private fun requestReadExternalPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            activity?.requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_32, FConstants.Permit.READ_EXTERNAL.index)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            activity?.requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_33, FConstants.Permit.READ_EXTERNAL.index)
        } else {
            activity?.requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_34, FConstants.Permit.READ_EXTERNAL.index)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ediUploadEvent(ediUploadEvent: EDIUploadEvent) {
        loading(false)
        if (ediUploadEvent.ediPK.isEmpty()) {
            toast(getResString(R.string.edi_file_upload_fail))
        } else {
            toast(getResString(R.string.edi_file_upload_comp))
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("spinnerEDIType")
        fun setSpinnerEDIType(spinner: Spinner, listItems: StateFlow<MutableList<EDIType>>?) {
            spinner.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                listItems?.collectLatest { x ->
                    spinner.adapter = ArrayAdapter(spinner.context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, x.map { y -> y.getDesc(spinner.context) })
                }
            }
        }
    }
}
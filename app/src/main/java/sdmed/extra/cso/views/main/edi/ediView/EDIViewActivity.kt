package sdmed.extra.cso.views.main.edi.ediView

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.EdiViewActivityBinding
import sdmed.extra.cso.models.adapter.EllipseListAdapter
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.MediaViewParcelModel
import sdmed.extra.cso.models.common.SelectListModel
import sdmed.extra.cso.models.eventbus.EDIUploadEvent
import sdmed.extra.cso.models.retrofit.edi.EDIPharmaBuffModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadPharmaModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadResponseModel
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.utils.FCameraUtil
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import sdmed.extra.cso.utils.FStorage.getParcelableList
import sdmed.extra.cso.utils.FStorage.putParcelable
import sdmed.extra.cso.utils.FStorage.putParcelableList
import sdmed.extra.cso.views.dialog.select.SelectDialog
import sdmed.extra.cso.views.hospitalMap.hospitalTempDetail.HospitalTempDetailActivity
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import sdmed.extra.cso.views.media.view.MediaListViewActivity
import sdmed.extra.cso.views.media.view.MediaViewActivity
import java.io.File
import java.util.ArrayList

class EDIViewActivity: FBaseActivity<EdiViewActivityBinding, EDIViewActivityVM>(UserRoles.of(UserRole.Admin, UserRole.CsoAdmin, UserRole.BusinessMan)) {
    override var layoutId = R.layout.edi_view_activity
    override val dataContext: EDIViewActivityVM by lazy {
        EDIViewActivityVM(multiDexApplication)
    }
    private var _bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var _bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null
    private var _cameraUtil: FCameraUtil? = null
    private var _cameraResult: ActivityResultLauncher<Intent>? = null
    private var _imagePickerResult: ActivityResultLauncher<Intent>? = null
    private var _externalManageResult: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityResult()
    }
    override fun viewInit() {
        super.viewInit()
        dataContext.thisPK = intent.getStringExtra("thisPK") ?: ""
        setEDIPharmaAdapter()
        setEDIFileAdapter()
        setEllipseAdapter()
        setEDIResponseAdapter()
        getData()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setEDIPharmaCommand(data)
        setEDIFileCommand(data)
        setEDIPharmaFileCommand(data)
        setEDIResponseCommand(data)
        setUploadBuffCommand(data)
        setSelectDialogCommand(data)
    }
    override fun onDestroy() {
        _bottomSheetCallback?.let { _bottomSheetBehavior?.removeBottomSheetCallback(it) }
        _bottomSheetCallback = null
        _bottomSheetBehavior = null
        _cameraUtil = null
        _cameraResult?.unregister()
        _cameraResult = null
        _imagePickerResult?.unregister()
        _imagePickerResult = null
        _externalManageResult?.unregister()
        _externalManageResult = null
        super.onDestroy()
    }


    private fun registerActivityResult() {
        _cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            val pharmaBuffPK = _cameraUtil?.targetPK ?: return@registerForActivityResult
            val cameraPath = _cameraUtil?.path ?: return@registerForActivityResult
            val imageNames = "edi_shot${FExtensions.getTodayString()}.jpeg"
            val imagePath = FImageUtils.uriCopyToTempFolder(this, File(cameraPath), imageNames).toString()
            val fileType: MediaFileType = MediaFileType.IMAGE
            dataContext.addImage(pharmaBuffPK, imagePath, imageNames, fileType, FContentsType.type_jpeg)
        }
        _imagePickerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            val pharmaBuffPK = it.data?.getStringExtra(FConstants.MEDIA_TARGET_PK) ?: ""
            val mediaList = it.data?.getParcelableList<MediaPickerSourceModel>(FConstants.MEDIA_LIST)
            dataContext.reSetImage(pharmaBuffPK, mediaList)
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
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? EDIViewActivityVM.ClickEvent ?: return
        when (eventName) {
            EDIViewActivityVM.ClickEvent.CLOSE -> close()
            EDIViewActivityVM.ClickEvent.HOSPITAL_DETAIL -> hospitalTempDetail()
        }
    }
    private fun setEDIPharmaCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIUploadPharmaModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIUploadPharmaModel ?: return
        when (eventName) {
            EDIUploadPharmaModel.ClickEvent.OPEN -> dataBuff.isOpen.value = !dataBuff.isOpen.value
            EDIUploadPharmaModel.ClickEvent.ADD -> addImage(dataBuff)
            EDIUploadPharmaModel.ClickEvent.SAVE -> save(dataBuff)
        }
    }
    private fun setEDIFileCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIUploadFileModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIUploadFileModel ?: return
        when (eventName) {
            EDIUploadFileModel.ClickEvent.SHORT -> {
                startActivity((Intent(this, MediaViewActivity::class.java).apply {
                    putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().parse(dataBuff))
                }))
            }
            EDIUploadFileModel.ClickEvent.LONG -> {
                startActivity((Intent(this, MediaListViewActivity::class.java).apply {
                    putParcelableList(FConstants.MEDIA_LIST, dataContext.getMediaViewFiles())
                }))
            }
        }
    }
    private fun setEDIPharmaFileCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIUploadPharmaFileModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIUploadPharmaFileModel ?: return
        when (eventName) {
            EDIUploadPharmaFileModel.ClickEvent.SHORT -> {
                startActivity((Intent(this, MediaViewActivity::class.java).apply {
                    putParcelable(FConstants.MEDIA_ITEM, MediaViewParcelModel().parse(dataBuff))
                }))
            }
            EDIUploadPharmaFileModel.ClickEvent.LONG -> {
                startActivity((Intent(this, MediaListViewActivity::class.java).apply {
                    putParcelableList(FConstants.MEDIA_LIST, dataContext.getMediaViewPharmaFiles(dataBuff))
                }))
            }
        }
    }
    private fun setEDIResponseCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIUploadResponseModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIUploadResponseModel ?: return
        when (eventName) {
            EDIUploadResponseModel.ClickEvent.OPEN -> {
                dataBuff.open = !dataBuff.open
                getResponseAdapter()?.updateItem(dataBuff)
            }
        }
    }
    private fun setUploadBuffCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? MediaPickerSourceModel.ClickEvent ?: return
        val dataBuff = data[1] as? MediaPickerSourceModel ?: return
        when (eventName) {
            MediaPickerSourceModel.ClickEvent.SELECT -> dataContext.delImage(dataBuff.thisPK)
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
        val pharmaBuffPK = (data as? EDIUploadPharmaModel)?.thisPK ?: ""
        _cameraUtil = FCameraUtil(this, cameraResult).apply { showCamera(pharmaBuffPK) }
    }
    private fun imagePickerOpen(data: Any?) {
        _imagePickerResult?.launch(Intent(this, MediaPickerActivity::class.java).apply {
            (data as? EDIUploadPharmaModel)?.let {
                putExtra(FConstants.MEDIA_TARGET_PK, it.thisPK)
                putParcelableList(FConstants.MEDIA_LIST, ArrayList(it.uploadItems.value))
            }
        })
    }

    private fun setEDIPharmaAdapter() = EDIViewPharmaAdapter(dataContext.relayCommand).also{ binding?.rvPharmaList?.adapter = it }
    private fun setEDIFileAdapter() {
        val binding = super.binding ?: return
        binding.vpEdiFileList.adapter = EDIViewFileAdapter(dataContext.relayCommand)
        binding.vpEdiFileList.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateEllipseList(position)
            }
        })
    }
    private fun setEllipseAdapter() = EllipseListAdapter(dataContext.relayCommand).also { binding?.rvEllipseList?.adapter = it }
    private fun setEDIResponseAdapter() = EDIViewResponseAdapter(dataContext.relayCommand).also { binding?.rvResponseList?.adapter = it }
    private fun updateEllipseList(position: Int) {
        val buff = dataContext.ellipseList.value
        if (position < 0 || position >= buff.size) {
            return
        }
        buff.forEach { x -> x.initThis() }
        buff[position].selectThis()
        buff.getOrNull(position - 3)?.tinyThis()
        buff.getOrNull(position - 2)?.visibleThis()
        buff.getOrNull(position - 1)?.visibleThis()
        buff.getOrNull(position + 1)?.visibleThis()
        buff.getOrNull(position + 2)?.visibleThis()
        buff.getOrNull(position + 3)?.tinyThis()
    }

    private fun getData() {
        loading()
        FCoroutineUtil.coroutineScope({
            val ret = dataContext.getData()
            loading(false)
            if (ret.result != true || ret.data == null) {
                toast(ret.msg)
                return@coroutineScope
            }
            binding?.vpEdiFileList?.isUserInputEnabled = dataContext.item.value.fileList.size > 1
            updateEllipseList(0)
        })
    }
    private fun close() {
        finish()
    }
    private fun hospitalTempDetail() {
        startActivity(Intent(this, HospitalTempDetailActivity::class.java).apply {
            putExtra(FConstants.HOSPITAL_PK, dataContext.item.value.tempHospitalPK)
        })
    }
    private fun addImage(data: EDIUploadPharmaModel) {
        if (!data.isAddable) {
            return
        }
        val items = mutableListOf<SelectListModel>()
        items.add(SelectListModel().apply {
            itemIndex = 0
            iconResId = R.drawable.vector_camera_button
            stringResId = R.string.camera_desc
            this.data = data
        })
        items.add(SelectListModel().apply {
            itemIndex = 1
            iconResId = R.drawable.vector_file_button
            stringResId = R.string.file_desc
            this.data = data
        })
        SelectDialog(items, 0, dataContext.relayCommand).show(supportFragmentManager, "")
    }
    private fun save(data: EDIUploadPharmaModel) {
        if (!data.isAddable) {
            return
        }
        if (!data.isSavable.value) {
            return
        }

        dataContext.startBackgroundService(data)
        toast(R.string.edi_file_upload)
        finish()
    }

    private fun getResponseAdapter(): EDIViewResponseAdapter? {
        return (binding?.rvResponseList?.adapter as? EDIViewResponseAdapter)
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
        requestPermissions(FConstants.CAMERA_PERMISSION, FConstants.Permit.CAMERA.index)
    }
    private fun requestReadExternalPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_32, FConstants.Permit.READ_EXTERNAL.index)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_33, FConstants.Permit.READ_EXTERNAL.index)
        } else {
            requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_34, FConstants.Permit.READ_EXTERNAL.index)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ediUploadEvent(ediUploadEvent: EDIUploadEvent) {
        if (dataContext.thisPK == ediUploadEvent.ediPK) {
            getData()
        }
    }
    companion object {
        @JvmStatic
        @BindingAdapter("viewPagerEDIFileList")
        fun setViewPagerEDIFileList(viewPager2: ViewPager2, item: StateFlow<EDIUploadModel>?) {
            val adapter = viewPager2.adapter as? EDIViewFileAdapter ?: return
            viewPager2.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                item?.collectLatest {
                    adapter.updateItems(it.fileList)
                }
            }
        }
        @JvmStatic
        @BindingAdapter("recyclerEDIPharmaList")
        fun setEDIPharmaList(recyclerView: RecyclerView, item: StateFlow<EDIUploadModel>?) {
            val adapter = recyclerView.adapter as? EDIViewPharmaAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                item?.collectLatest {
                    adapter.updateItems(it.pharmaList)
                }
            }
        }
        @JvmStatic
        @BindingAdapter("recyclerEDIResponseList")
        fun setEDIResponseList(recyclerView: RecyclerView, item: StateFlow<EDIUploadModel>?) {
            val adapter = recyclerView.adapter as? EDIViewResponseAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                item?.collectLatest {
                    adapter.updateItems(it.responseList)
                }
            }
        }
    }
}
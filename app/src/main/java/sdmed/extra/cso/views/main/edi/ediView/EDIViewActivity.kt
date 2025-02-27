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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.EdiViewActivityBinding
import sdmed.extra.cso.models.adapter.UploadBuffAdapter
import sdmed.extra.cso.models.adapter.EllipseListAdapter
import sdmed.extra.cso.models.common.EllipseItemModel
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.SelectListModel
import sdmed.extra.cso.models.eventbus.EDIUploadEvent
import sdmed.extra.cso.models.retrofit.edi.EDIUploadFileModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadModel
import sdmed.extra.cso.models.retrofit.edi.EDIUploadResponseModel
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.utils.FCameraUtil
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import sdmed.extra.cso.views.dialog.select.SelectDialog
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
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
        setUploadBuffAdapter()
        getData()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setEDIFileCommand(data)
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
            val cameraPath = _cameraUtil?.path ?: return@registerForActivityResult
            val imageNames = "edi_shot${FExtensions.getTodayString()}.jpeg"
            val imagePath = FImageUtils.uriCopyToTempFolder(this, File(cameraPath), imageNames).toString()
            val fileType: MediaFileType = MediaFileType.IMAGE
            dataContext.addImage(imagePath, imageNames, fileType, FContentsType.type_jpeg)
            savableCheck()
        }
        _imagePickerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }
            val mediaList =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getParcelableArrayListExtra("mediaList", MediaPickerSourceModel::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getParcelableArrayListExtra<MediaPickerSourceModel>("mediaList")
                }
            dataContext.reSetImage(mediaList)
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
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? EDIViewActivityVM.ClickEvent ?: return
        when (eventName) {
            EDIViewActivityVM.ClickEvent.CLOSE -> close()
            EDIViewActivityVM.ClickEvent.ADD_IMAGE -> addImage()
            EDIViewActivityVM.ClickEvent.SAVE -> save()
        }
    }
    private fun setEDIFileCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? EDIUploadFileModel.ClickEvent ?: return
        val dataBuff = data[1] as? EDIUploadFileModel ?: return
        when (eventName) {
            EDIUploadFileModel.ClickEvent.THIS -> {
                toast("edi LongClick ${dataBuff.originalFilename}")
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
            MediaPickerSourceModel.ClickEvent.SELECT ->  {
                dataContext.removeImage(dataBuff)
                savableCheck()
            }
            else -> { }
        }
    }
    private fun setSelectDialogCommand(data: Any?) {
        if (dataContext.isAddable.value == false) {
            return
        }

        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? SelectListModel.ClickEvent ?: return
        val dataBuff = data[1] as? SelectListModel ?: return
        when (eventName) {
            SelectListModel.ClickEvent.SELECT -> {
                if (dataBuff.itemIndex == 0) {
                    if (hasCameraGranted()) {
                        cameraOpen()
                    } else {
                        requestCameraPermissions()
                    }
                } else if (dataBuff.itemIndex == 1) {
                    if (hasReadExternalGranted()) {
                        imagePickerOpen()
                    } else {
                        requestReadExternalPermissions()
                    }
                }
            }
        }
    }
    private fun cameraOpen() {
        val cameraResult = _cameraResult ?: return
        _cameraUtil = FCameraUtil(this, cameraResult).apply { showCamera() }
    }
    private fun imagePickerOpen() {
        _imagePickerResult?.launch(Intent(this, MediaPickerActivity::class.java).apply {
            putParcelableArrayListExtra("mediaList", dataContext.getMediaItems())
        })
    }
    private fun savableCheck() {
        if (dataContext.isAddable.value == false) {
            return
        }

        dataContext.isSavable.value = dataContext.uploadItems.value.isNotEmpty()
    }

    private fun setEDIPharmaAdapter() {
        val binding = super.binding ?: return
        binding.rvPharmaList.adapter = EDIViewPharmaAdapter(dataContext.relayCommand)
    }
    private fun setEDIFileAdapter() {
        val binding = super.binding ?: return
        binding.vpEdiFileList.adapter = EDIViewFileAdapter(dataContext.relayCommand)
        binding.vpEdiFileList.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateEllipseList(position)
            }
        })
    }
    private fun setEllipseAdapter() = binding?.rvEllipseList?.adapter = EllipseListAdapter(dataContext.relayCommand)
    private fun setEDIResponseAdapter() = binding?.rvResponseList?.adapter = EDIViewResponseAdapter(dataContext.relayCommand)
    private fun setUploadBuffAdapter() = binding?.rvUploadBuffList?.adapter = UploadBuffAdapter(dataContext.relayCommand)
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
    private fun addImage() {
        dataContext.isSavable.value = false
        if (!dataContext.item.value.ediState.isEditable()) {
            return
        }
        val items = mutableListOf<SelectListModel>()
        items.add(SelectListModel().apply {
            itemIndex = 0
            iconResId = R.drawable.vector_camera_def
            stringResId = R.string.camera_desc
        })
        items.add(SelectListModel().apply {
            itemIndex = 1
            iconResId = R.drawable.vector_file_def
            stringResId = R.string.file_desc
        })
        SelectDialog(items, 0, dataContext.relayCommand).show(supportFragmentManager, "")
    }
    private fun save() {
        if (!dataContext.item.value.ediState.isEditable()) {
            return
        }
        if (dataContext.uploadItems.value.size <= 0) {
            return
        }
        if (!dataContext.isSavable.value) {
            return
        }

        dataContext.startBackgroundService()
        toast(R.string.edi_file_upload)
        savableCheck()
//        loading()
//        val ediUploadFileModel = mutableListOf<EDIUploadFileModel>()
//        FCoroutineUtil.coroutineScope({
//            val ret = dataContext.postFile(ediUploadFileModel)
//            loading(false)
//            if (ret.result != true || ret.data == null) {
//                toast(ret.msg)
//                return@coroutineScope
//            }
//            dataContext.uploadItems.value = mutableListOf()
//            getData()
//        })
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
        if (shouldShowRequestPermissionRationale(FConstants.CAMERA_PERMISSION)) {
            requestPermissions(FConstants.CAMERA_PERMISSION, FConstants.Permit.CAMERA.index)
        }
    }
    private fun requestReadExternalPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (shouldShowRequestPermissionRationale(FConstants.READ_EXTERNAL_PERMISSION_32)) {
                requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_32, FConstants.Permit.READ_EXTERNAL.index)
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            if (shouldShowRequestPermissionRationale(FConstants.READ_EXTERNAL_PERMISSION_33)) {
                requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_33, FConstants.Permit.READ_EXTERNAL.index)
            }
        } else {
            if (shouldShowRequestPermissionRationale(FConstants.READ_EXTERNAL_PERMISSION_34)) {
                requestPermissions(FConstants.READ_EXTERNAL_PERMISSION_34, FConstants.Permit.READ_EXTERNAL.index)
            }
        }
    }

    private fun uploadBuffAdapter(): UploadBuffAdapter? {
        return binding?.rvUploadBuffList?.adapter as? UploadBuffAdapter
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
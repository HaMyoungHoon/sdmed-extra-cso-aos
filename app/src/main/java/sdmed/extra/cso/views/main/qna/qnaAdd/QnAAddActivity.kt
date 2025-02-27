package sdmed.extra.cso.views.main.qna.qnaAdd

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.QnaAddActivityBinding
import sdmed.extra.cso.models.adapter.UploadBuffAdapter
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.SelectListModel
import sdmed.extra.cso.models.retrofit.users.UserRole
import sdmed.extra.cso.models.retrofit.users.UserRoles
import sdmed.extra.cso.utils.FCameraUtil
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import sdmed.extra.cso.views.dialog.select.SelectDialog
import sdmed.extra.cso.views.media.picker.MediaPickerActivity
import java.io.File
import java.util.ArrayList

class QnAAddActivity: FBaseActivity<QnaAddActivityBinding, QnAAddActivityVM>(UserRoles.of(UserRole.Admin, UserRole.CsoAdmin, UserRole.BusinessMan)) {
    override var layoutId = R.layout.qna_add_activity
    override val dataContext: QnAAddActivityVM by lazy {
        QnAAddActivityVM(multiDexApplication)
    }
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
        dataContext.thisPK.value = intent.getStringExtra("thisPK") ?: ""
        dataContext.title.value = intent.getStringExtra("title") ?: getString(R.string.qna_upload)
        setUploadBuffAdapter()
        observeText()
    }
    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
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
        val eventName = data as? QnAAddActivityVM.ClickEvent ?: return
        when (eventName) {
            QnAAddActivityVM.ClickEvent.CLOSE -> close()
            QnAAddActivityVM.ClickEvent.ADD -> addImage()
            QnAAddActivityVM.ClickEvent.SAVE -> save()
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
        var titleCheck = dataContext.postTitle.value.isNotEmpty()
        if (dataContext.thisPK.value.isNotEmpty()) {
            titleCheck = true
        }
        val contentCheck = dataContext.content.value.isNotEmpty()
        dataContext.isSavable.value = titleCheck && contentCheck
    }
    private fun setUploadBuffAdapter() = binding?.rvUploadBuffList?.adapter = UploadBuffAdapter(dataContext.relayCommand)
    private fun close() {
        setResult(RESULT_CANCELED)
        finish()
    }
    private fun addImage() {
        dataContext.isSavable.value = false
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
        if (!dataContext.isSavable.value) {
            return
        }

        dataContext.startBackgroundService()
        toast(R.string.qna_upload)
        setResult(RESULT_OK)
        finish()
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
    private fun observeText() {
        lifecycleScope.launch {
            async { dataContext.content.collectLatest { savableCheck() } }.await()
            async { dataContext.postTitle.collectLatest { savableCheck() } }.await()
        }
    }
}
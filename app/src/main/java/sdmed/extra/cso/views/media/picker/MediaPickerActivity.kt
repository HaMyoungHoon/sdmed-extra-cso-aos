package sdmed.extra.cso.views.media.picker

import android.content.ContentUris
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseActivity
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.databinding.MediaPickerActivityBinding
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.models.common.SelectListModel
import sdmed.extra.cso.utils.FContentsType
import sdmed.extra.cso.utils.FCoroutineUtil
import sdmed.extra.cso.utils.FExtensions
import sdmed.extra.cso.utils.FImageUtils
import sdmed.extra.cso.views.dialog.select.SelectDialog

class MediaPickerActivity: FBaseActivity<MediaPickerActivityBinding, MediaPickerActivityVM>() {
    override var layoutId = R.layout.media_picker_activity
    override val dataContext: MediaPickerActivityVM by lazy {
        MediaPickerActivityVM(multiDexApplication)
    }
    private var previousView: PlayerView? = null

    override fun onDestroy() {
        playerRelease()
        binding?.playerView?.player = null
        super.onDestroy()
    }
    override fun onPause() {
        playerStop()
        super.onPause()
    }
    override fun onStart() {
        playerPlay()
        super.onStart()
    }

    override fun viewInit() {
        getPermission()
        super.viewInit()
    }
    private fun init() {
        val mediaList = getImageList()
//        getVideoList(mediaList)
        getFileList(mediaList)
        dataContext.setItems(mediaList)
        dataContext.selectItem(0)
        binding?.rvMediaList?.adapter = MediaPickerActivityAdapter(dataContext.relayCommand)
        lifecycleScope.launch {
            dataContext.boxesPosition.collectLatest {
                dataContext.selectItem(it)
            }
        }
        val buffList =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra("mediaList", MediaPickerSourceModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra<MediaPickerSourceModel>("mediaList")
            }
        dataContext.ableSelectCountStringSuffix = getString(R.string.media_able_click_suffix_desc)
        dataContext.setPreviousMedia(buffList)
        dataContext.setMediaMaxCount(intent.getIntExtra("mediaMaxCount", -1))
        binding?.playerView?.player = ExoPlayer.Builder(this).build()
    }

    override fun setLayoutCommand(data: Any?) {
        setThisCommand(data)
        setMediaItemCommand(data)
//        setVideoCommand(data)
        setDialogCommand(data)
    }
    private fun setThisCommand(data: Any?) {
        val eventName = data as? MediaPickerActivityVM.ClickEvent ?: return
        when (eventName) {
            MediaPickerActivityVM.ClickEvent.CLOSE -> {
                setResult(RESULT_CANCELED)
                Glide.get(this).clearMemory()
                finish()
            }
            MediaPickerActivityVM.ClickEvent.CONFIRM -> {
                if (!dataContext.confirmEnable.value) {
                    return
                }
                setResult(RESULT_OK, Intent().apply {
                    putParcelableArrayListExtra("mediaList", dataContext.getClickItems())
                })
                Glide.get(this).clearMemory()
                finish()
            }
        }
    }
    private fun setMediaItemCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? MediaPickerSourceModel.ClickEvent ?: return
        val dataBuff = data[1] as? MediaPickerSourceModel ?: return
        when (eventName) {
            MediaPickerSourceModel.ClickEvent.SELECT -> {
                val findItem = dataContext.clickItemBuff.firstOrNull { it.mediaPath == dataBuff.mediaPath }
                if (findItem != null) {
                    if (dataContext.mediaPath.value == findItem.mediaPath) {
                        dataContext.removeClickedItem(dataBuff)
                        dataContext.confirmEnable.value = dataContext.getClickItemCount() > 0
                        val lastItem = dataContext.clickItemBuff.lastOrNull()
                        if (lastItem != null) {
                            dataContext.setLastClickedItem(lastItem)
                            dataContext.mediaPath.value = lastItem.mediaPath
                            dataContext.mediaFileType.value = lastItem.mediaFileType
                            dataContext.mediaName.value = lastItem.mediaName
                        }
                    } else {
                        dataContext.setLastClickedItem(dataBuff)
                        dataContext.mediaPath.value = findItem.mediaPath
                        dataContext.mediaFileType.value = findItem.mediaFileType
                        dataContext.mediaName.value = findItem.mediaName
                    }
                    playerPlay()
                    getMediaPickerAdapter()?.updateItem(dataBuff)
                    dataContext.clickItemBuff.forEach {
                        getMediaPickerAdapter()?.updateItem(it)
                    }
                    return
                }
                if (!dataContext.isPossibleSelect() && !dataBuff.clickState) {
                    return
                }

                dataContext.setLastClickedItem(dataBuff)
                dataContext.appendClickedItem(dataBuff)
                dataContext.mediaPath.value = dataBuff.mediaPath
                dataContext.mediaFileType.value = dataBuff.mediaFileType
                dataContext.mediaName.value = dataBuff.mediaName
                playerPlay()

                getMediaPickerAdapter()?.updateItem(dataBuff)
                dataContext.clickItemBuff.forEach {
                    getMediaPickerAdapter()?.updateItem(it)
                }
                dataContext.confirmEnable.value = dataContext.getClickItemCount() > 0
            }
            MediaPickerSourceModel.ClickEvent.SELECT_LONG -> {
                val items = mutableListOf<SelectListModel>()
                items.add(SelectListModel().apply {
                    itemIndex = 1
                    iconResId = R.drawable.vector_delete_def
                    stringResId = R.string.remove_desc
                    this.data = dataBuff
                })
                SelectDialog(items, 0, dataContext.relayCommand).show(supportFragmentManager, "")
            }
        }
    }
    private fun setVideoCommand(data: Any?) {
        if (data !is ArrayList<*> || data.size <= 1) return
        val view = data[0] as? PlayerView ?: return
        val dataBuff = data[1] as? MediaPickerSourceModel ?: return
        dataBuff.clickState = !dataBuff.clickState
        if (dataBuff.clickState) {
            dataContext.appendClickedItem(dataBuff)
        } else {
            dataContext.removeClickedItem(dataBuff)
        }
        previousView?.player?.stop()
        previousView = view
        previousView?.player?.prepare()
        previousView?.player?.play()
        dataContext.confirmEnable.value = dataContext.getClickItemCount() > 0
    }
    private fun setDialogCommand(data: Any?) {
        if (data !is java.util.ArrayList<*> || data.size <= 1) return
        val eventName = data[0] as? SelectListModel.ClickEvent ?: return
        val dataBuff = data[1] as? SelectListModel ?: return
        val modelBuff = dataBuff.data as? MediaPickerSourceModel ?: return
        when (eventName) {
            SelectListModel.ClickEvent.SELECT -> {
                val uri = modelBuff.mediaPath
                if (uri != null) {
                    dataContext.removeClickedItem(modelBuff)
                    dataContext.removeItems(modelBuff)
                    try {
                        FImageUtils.fileDelete(this, uri)
                    } catch (_: Exception) {

                    }
                }
            }
        }
    }

    private fun getPermission() {
        val permissions = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            FConstants.READ_EXTERNAL_PERMISSION_32
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            FConstants.READ_EXTERNAL_PERMISSION_33
        } else {
            FConstants.READ_EXTERNAL_PERMISSION_34
        }
        FCoroutineUtil.coroutineScope({
            TedPermission.create()
                .setRationaleTitle(R.string.permit_album)
                .setRationaleMessage(R.string.permit_read_request)
                .setDeniedTitle(R.string.cancel_desc)
                .setDeniedMessage(R.string.permit_denied)
                .setGotoSettingButtonText(R.string.permit_setting)
                .setPermissions(*permissions)
                .check()
        }, {
            if (!it.isGranted) {
                finish()
            } else {
                init()
            }
        })
    }
    private fun getFileList(buff: MutableList<Pair<String, MutableList<MediaPickerSourceModel>>>?): MutableList<Pair<String, MutableList<MediaPickerSourceModel>>> {
        val mediaList = buff ?: mutableListOf()
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.DISPLAY_NAME, MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME, MediaStore.Files.FileColumns.DATE_ADDED, MediaStore.Files.FileColumns.MIME_TYPE)
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} IN (?, ?, ?, ?)"
        val selectionArgs = arrayOf(FContentsType.type_pdf, FContentsType.type_xlsx, FContentsType.type_xls, FContentsType.type_zip)

        val query = this.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val folderColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
            val dateTimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
            while (cursor.moveToNext()) {
                val uri = cursor.getString(dataColumn)
                val name = cursor.getString(nameColumn)
                val folderName = cursor.getString(folderColumn)
                val dateTime = cursor.getString(dateTimeColumn)
                val mimeString = cursor.getString(mimeTypeColumn)
                if (mimeString != FContentsType.type_pdf && mimeString != FContentsType.type_xlsx && mimeString != FContentsType.type_xls && mimeString != FContentsType.type_zip) {
                    continue
                }
                val findFolder = mediaList.find { x -> x.first == folderName }
                if (findFolder != null) {
                    findFolder.second.add(
                        MediaPickerSourceModel(
                            uri.toUri(),
                            name,
                            MediaFileType.fromMimeType(mimeString),
                            dateTime,
                            mimeString
                        )
                    )
                } else {
                    mediaList.add(Pair(folderName, arrayListOf(
                        MediaPickerSourceModel(
                            uri.toUri(),
                            name,
                            MediaFileType.fromMimeType(mimeString),
                            dateTime,
                            mimeString
                        )
                    )))
                }
            }
        }
        return mediaList
    }
    private fun getImageList(): MutableList<Pair<String, MutableList<MediaPickerSourceModel>>> {
        val mediaList = mutableListOf<Pair<String, MutableList<MediaPickerSourceModel>>>()
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.MIME_TYPE)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query = this.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val folderColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val dateTimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val folderName = cursor.getString(folderColumn)
                val dateTime = cursor.getString(dateTimeColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                val findFolder = mediaList.find { x -> x.first == folderName }
                if (findFolder != null) {
                    findFolder.second.add(
                        MediaPickerSourceModel(
                            uri,
                            name,
                            MediaFileType.IMAGE,
                            dateTime,
                            mimeType,
                        )
                    )
                } else {
                    mediaList.add(Pair(folderName, arrayListOf(
                        MediaPickerSourceModel(
                            uri,
                            name,
                            MediaFileType.IMAGE,
                            dateTime,
                            mimeType,
                        )
                    )))
                }
            }
        }
        return mediaList
    }
    private fun getVideoList(buff: MutableList<Pair<String, MutableList<MediaPickerSourceModel>>>?): MutableList<Pair<String, MutableList<MediaPickerSourceModel>>> {
        val mediaList = buff ?: mutableListOf()
        val projection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATE_ADDED, MediaStore.Video.Media.DURATION)
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        val query = this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )
        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val folderColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val dateTimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val folderName = cursor.getString(folderColumn)
                val dateTime = cursor.getString(dateTimeColumn)
                val duration = cursor.getLong(durationColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                val findFolder = mediaList.find { x -> x.first == folderName }
                if (findFolder != null) {
                    findFolder.second.add(
                        MediaPickerSourceModel(
                            uri,
                            name,
                            MediaFileType.VIDEO,
                            dateTime,
                            this.contentResolver.getType(uri) ?: "",
                            duration
                        ).generateData())
                } else {
                    mediaList.add(Pair(folderName, arrayListOf(
                        MediaPickerSourceModel(
                            uri,
                            name,
                            MediaFileType.VIDEO,
                            dateTime,
                            this.contentResolver.getType(uri) ?: "",
                            duration
                        ).generateData())))
                }
            }
        }
        return mediaList
    }
    private fun playerPlay() {
        if (dataContext.mediaPath.value == null) {
            return
        }
        if (dataContext.mediaFileType.value != MediaFileType.VIDEO) {
            playerStop()
            return
        }
        dataContext.videoPath.value = dataContext.mediaPath.value
    }
    private fun playerStop() {
        val player = binding?.playerView?.player ?: return
        player.stop()
        dataContext.videoPath.value = null
    }
    private fun playerRelease() {
        val player = binding?.playerView?.player ?: return
        player.stop()
        player.release()
        dataContext.videoPath.value = null
    }
    private fun getMediaPickerAdapter(): MediaPickerActivityAdapter? {
        return binding?.rvMediaList?.adapter as? MediaPickerActivityAdapter
    }

    companion object {
        @JvmStatic
        @BindingAdapter("spinnerBoxMediaPickerItems")
        fun setMediaSpinnerItems(spinner: Spinner, spinnerBoxMediaPickerItems: StateFlow<MutableList<String>>?) {
            spinner.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                spinnerBoxMediaPickerItems?.collectLatest {
                    spinner.adapter = ArrayAdapter(spinner.context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, it)
                }
            }
        }
        @JvmStatic
        @BindingAdapter("recyclerMediaPickerItems")
        fun setMediaRecyclerItems(recyclerView: RecyclerView, recyclerMediaPickerItems: StateFlow<MutableList<MediaPickerSourceModel>>?) {
            val adapter = recyclerView.adapter as? MediaPickerActivityAdapter ?: return
            recyclerView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                recyclerMediaPickerItems?.collectLatest {
                    adapter.updateItems(it)
                }
            }
        }
    }
}
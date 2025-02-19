package sdmed.extra.cso.views.media.picker

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import androidx.multidex.MultiDexApplication
import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.R
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.models.common.MediaFileType
import sdmed.extra.cso.models.common.MediaPickerSourceModel
import sdmed.extra.cso.utils.FContentsType
import java.io.File

class MediaPickerActivityVM(application: MultiDexApplication): FBaseViewModel(application) {
    val mediaMaxCount = MutableStateFlow(-1)
    var ableSelectCountStringSuffix = ""
    val boxesPosition = MutableStateFlow(0)
    val boxes = MutableStateFlow(mutableListOf(""))
    private val _selectBox = mutableListOf<Pair<String, MutableList<MediaPickerSourceModel>>>()
    val selectableCountString = MutableStateFlow("")
    val confirmEnable = MutableStateFlow(false)
    val items = MutableStateFlow(mutableListOf<MediaPickerSourceModel>())
    val clickItemBuff = mutableListOf<MediaPickerSourceModel>()
    val mediaFileType = MutableStateFlow(MediaFileType.UNKNOWN)
    val mediaPath = MutableStateFlow<Uri?>(null)
    val videoPath = MutableStateFlow<Uri?>(null)

    fun setPreviousMedia(mediaPathList: ArrayList<String>?, mediaNameList: ArrayList<String>?, mediaFileTypeList: ArrayList<Int>?, mediaMimeType: ArrayList<String>?) {
        mediaPathList?.forEachIndexed { index, x ->
            val mediaName = if (!mediaNameList.isNullOrEmpty() && mediaNameList.size > index) mediaNameList[index] else ""
            val mediaFileType = if (!mediaFileTypeList.isNullOrEmpty() && mediaFileTypeList.size > index) mediaFileTypeList[index] else 0
            val mimeType = if (!mediaMimeType.isNullOrEmpty() && mediaMimeType.size > index) mediaMimeType[index] else FContentsType.type_jpeg
            clickItemBuff.add(
                MediaPickerSourceModel(
                    x.toUri(),
                    mediaName,
                    MediaFileType.fromIndex(mediaFileType),
                    "",
                    mimeType,
                    0,
                    true,
                    index + 1,
                    R.color.primary
                )
            )
        }
    }
    fun setPreviousMedia(mediaPickerModel: ArrayList<MediaPickerSourceModel>?) {
        mediaPickerModel?.forEach { x ->
            clickItemBuff.add(x)
        }
    }
    fun setMediaMaxCount(data: Int) {
        mediaMaxCount.value = data
        selectableCountString.value = if (data < 0) "" else "$data $ableSelectCountStringSuffix"
    }
    private fun getAbleSelectCountString() = if (mediaMaxCount.value < 0) "" else "${mediaMaxCount.value - getClickItemCount()} $ableSelectCountStringSuffix"
    fun isPossibleSelect() = mediaMaxCount.value < 0 || mediaMaxCount.value > getClickItemCount()

    fun getClickItems() = ArrayList(clickItemBuff.toMutableList())
    fun getClickItemCount() = clickItemBuff.size
    fun getClickedItemList() = ArrayList(clickItemBuff.map { it.mediaPath.toString() })
    fun getClickedItemNameList() = ArrayList(clickItemBuff.map { it.mediaName })
    fun getClickedItemFileTypeList() = ArrayList(clickItemBuff.map { it.mediaFileType.index })
    fun setLastClickedItem(data: MediaPickerSourceModel) {
        val itemsBuff = items.value.toMutableList()
        clickItemBuff.forEachIndexed { index, x ->
            itemsBuff.find { y -> x.mediaPath == y.mediaPath }?.let { z ->
                z.lastClick = false
            }
        }
        data.lastClick = true
        items.value = itemsBuff
    }
    fun appendClickedItem(data: MediaPickerSourceModel) {
        data.num = clickItemBuff.size + 1
        data.clickState = true
        data.solid = R.color.primary
        clickItemBuff.add(data)
        selectableCountString.value = getAbleSelectCountString()
    }
    fun removeClickedItem(data: MediaPickerSourceModel) {
        val itemsBuff = items.value.toMutableList()
        clickItemBuff.removeIf { it.mediaPath == data.mediaPath }
        clickItemBuff.forEachIndexed { index, x ->
            itemsBuff.find { y -> x.mediaPath == y.mediaPath }?.let { z ->
                z.num = index + 1
                z.clickState = true
                z.solid = R.color.primary
            }
        }
        itemsBuff.find { x -> x == data }?.let { y ->
            y.num = null
            y.clickState = false
            y.lastClick = false
            y.solid = null
        }
        items.value = itemsBuff
        selectableCountString.value = getAbleSelectCountString()
    }
    fun selectItem(data: Int) {
        if (data >= _selectBox.size || data < 0) {
            return
        }

        val itemBuff = mutableListOf<MediaPickerSourceModel>()
        itemBuff.addAll(_selectBox[data].second)
        clickItemBuff.forEachIndexed { index, x ->
            itemBuff.find { y -> x.mediaPath == y.mediaPath }?.let { z ->
                z.num = index + 1
                z.clickState = true
                z.solid = R.color.primary
            }
        }
        items.value = itemBuff
    }
    fun clearItem() {
        clickItemBuff.clear()
        _selectBox.clear()
        boxes.value = mutableListOf()
    }
    fun setItems(data: File) {
        data.listFiles()?.forEach { x -> addItems(x) }
    }
    fun setItems(data: List<Pair<String, MutableList<MediaPickerSourceModel>>>) {
        val boxesBuff = boxes.value.toMutableList()
        data.forEach { x ->
            if (boxesBuff.find { y -> y == x.first } != null) {
                return@forEach
            }
            boxesBuff.add(x.first)
            x.second.sortByDescending { it.mediaDateTime }
            _selectBox.add(Pair(x.first, x.second))
        }
        boxes.value = boxesBuff
    }
    fun removeItems(data: MediaPickerSourceModel) {
        items.value.find { x -> x.mediaPath == data.mediaPath }?.let {  y ->
            _selectBox.find { z -> z.second == items }?.second?.removeIf { z -> z == y }
            items.value = items.value.filter { z -> z != y }.toMutableList()
        }
    }
    private fun addItems(data: File?) {
        if (data == null) {
            return
        }
        if (data.isDirectory) {
            val thisPath = data.name.substring(data.name.lastIndexOf("/") + 1)
            if (boxes.value.find { x -> x == thisPath } != null) {
                return
            }
            data.listFiles()?.forEach { x ->
                val xName = x.name.substring(x.name.lastIndexOf("/") + 1)
                if (boxes.value.find { y -> y == xName } != null) {
                    return@forEach
                }
                if (xName.endsWith(".thumbnail")) {
                    return@forEach
                }
                if (x.isDirectory) {
                    addItems(x)
                }
            }
            data.listFiles()?.forEach { x ->
                if (x.isFile && isMediaFile(x) != MediaFileType.UNKNOWN) {
                    addMediaFile(x)
                }
            }
        }
    }
    private fun addMediaFile(data: File?) {
        if (data == null) {
            return
        }

        val itemBuff = mutableListOf<MediaPickerSourceModel>()
        val dataParent = data.parent ?: return
        val parentPath = dataParent.substring(dataParent.lastIndexOf("/") + 1)
        val boxesBuff = boxes.value.toMutableList()
        if (boxesBuff.find { it == parentPath } != null) {
            return
        }
        boxesBuff.add(parentPath)
        data.parentFile?.listFiles()?.forEach { x ->
            val fileType = isMediaFile(x)
            if (fileType != MediaFileType.UNKNOWN) {
                itemBuff.add(MediaPickerSourceModel(x.toUri(), mediaFileType = fileType))
            }
        }
    }
    fun isImageFile(file: File): MediaFileType {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val mimeType = mimeTypeMap.getMimeTypeFromExtension(file.extension)
        if (mimeType?.startsWith("image/") == true) {
            return MediaFileType.IMAGE
        }
        return MediaFileType.UNKNOWN
    }
    fun isVideoFile(file: File): MediaFileType {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val mimeType = mimeTypeMap.getMimeTypeFromExtension(file.extension)
        if (mimeType?.startsWith("video/") == true) {
            return MediaFileType.VIDEO
        }
        return MediaFileType.UNKNOWN
    }
    fun isMediaFile(file: File): MediaFileType {
        val isImage = isImageFile(file)
        if (isImage != MediaFileType.UNKNOWN) {
            return isImage
        }
        val isVideo =  isVideoFile(file)
        if (isVideo != MediaFileType.UNKNOWN) {
            return isVideo
        }

        return MediaFileType.UNKNOWN
    }
    fun isWebpFile(file: File): MediaFileType {
        if (file.extension.equals("webp", ignoreCase = true)) {
            val mimeType = getMimeType(file.absolutePath)
        }
        return MediaFileType.UNKNOWN
    }
    fun getMimeType(filePath: String): String? {
        val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    enum class ClickEvent(var index: Int) {
        CLOSE(0),
        CONFIRM(1)
    }
}
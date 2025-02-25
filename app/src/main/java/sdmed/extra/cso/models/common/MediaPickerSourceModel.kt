package sdmed.extra.cso.models.common

import android.net.Uri
import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.interfaces.command.ICommand
import sdmed.extra.cso.utils.FExtensions

@Parcelize
data class MediaPickerSourceModel(
    var mediaPath: Uri? = null,
    var mediaName: String = "",
    var mediaFileType: MediaFileType = MediaFileType.UNKNOWN,
    var mediaDateTime: String = "",
    var mediaMimeType: String = "",
    var duration: Long = -1L,
    var clickState: Boolean = false,
    var num: Int? = null,
    var solid: Int? = null,
    var stroke: Int? = null,
    var lastClick: Boolean = false,
    var durationString: String = "",
): FDataModelClass<MediaPickerSourceModel.ClickEvent>(), Parcelable {
    fun generateData(): MediaPickerSourceModel {
        durationString = FExtensions.getDurationToTime(duration)
        return this
    }
    fun onVideoClick(view: View) {
        relayCommand?.execute(arrayListOf(view, this))
    }
    enum class ClickEvent(var index: Int) {
        SELECT(0),
        SELECT_LONG(1),
    }
}
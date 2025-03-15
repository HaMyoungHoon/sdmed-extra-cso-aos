package sdmed.extra.cso.models.retrofit.hospitals
import sdmed.extra.cso.R
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.utils.googleMap.MarkerClusterDataModel

@Parcelize
data class HospitalTempModel(
    var thisPK: String = "",
    var code: String = "",
    var orgName: String = "",
    var hospitalTempTypeCode: HospitalTempTypeCode = HospitalTempTypeCode.CODE_00,
    var hospitalTempMetroCode: HospitalTempMetroCode = HospitalTempMetroCode.CODE_000000,
    var hospitalTempCityCode: HospitalTempCityCode = HospitalTempCityCode.CODE_000000,
    var hospitalTempLocalName: String = "",
    var zipCode: Int = 0,
    var address: String = "",
    var phoneNumber: String = "",
    var websiteUrl: String = "",
    var openDate: String = "",
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var fileList: MutableList<HospitalTempModel> = mutableListOf()
): FDataModelClass<HospitalTempModel.ClickEvent>(), Parcelable {
    @IgnoredOnParcel
    @Transient
    @JsonIgnore
    val isSelect = MutableStateFlow(false)

    fun toMarkerClusterDataModel(): MarkerClusterDataModel {
        val ret = MarkerClusterDataModel()
        ret.thisPK = this.thisPK
        ret.orgName = this.orgName
        ret.address = this.address
        ret.phoneNumber = this.phoneNumber
        ret.websiteUrl = this.websiteUrl
        ret.latitude = this.latitude
        ret.longitude = this.longitude
        ret.resDrawableId = R.drawable.hospital_red
        return ret
    }
    fun parse(data: MarkerClusterDataModel): HospitalTempModel {
        this.thisPK = data.thisPK
        this.orgName = data.orgName
        this.address = data.address
        this.phoneNumber = data.phoneNumber
        this.websiteUrl = data.websiteUrl
        this.latitude = data.latitude
        this.longitude = data.longitude
        return this
    }

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}
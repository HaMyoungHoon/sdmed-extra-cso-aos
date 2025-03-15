package sdmed.extra.cso.interfaces.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel

interface IHospitalTempService {
    @GET("${FConstants.REST_API_HOSPITAL_TEMP}/data/{thisPK}")
    suspend fun getData(@Path("thisPK") thisPK: String): RestResultT<HospitalTempModel>
    @GET("${FConstants.REST_API_HOSPITAL_TEMP}/list/search")
    suspend fun getListSearch(@Query("searchString") searchString: String): RestResultT<MutableList<HospitalTempModel>>
    @GET("${FConstants.REST_API_HOSPITAL_TEMP}/list/nearby")
    suspend fun getListNearby(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("distance") distance: Int = 1000): RestResultT<MutableList<HospitalTempModel>>
    @GET("${FConstants.REST_API_HOSPITAL_TEMP}/list/nearby/pharmacy")
    suspend fun getPharmacyListNearby(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("distance") distance: Int = 1000): RestResultT<MutableList<PharmacyTempModel>>
}
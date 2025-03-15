package sdmed.extra.cso.interfaces.repository

import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.hospitals.HospitalTempModel
import sdmed.extra.cso.models.retrofit.hospitals.PharmacyTempModel

interface IHospitalTempRepository {
    suspend fun getData(thisPK: String): RestResultT<HospitalTempModel>
    suspend fun getListSearch(searchString: String): RestResultT<MutableList<HospitalTempModel>>
    suspend fun getListNearby(latitude: Double, longitude: Double, distance: Int = 1000): RestResultT<MutableList<HospitalTempModel>>
    suspend fun getPharmacyListNearby(latitude: Double, longitude: Double, distance: Int = 1000): RestResultT<MutableList<PharmacyTempModel>>
}
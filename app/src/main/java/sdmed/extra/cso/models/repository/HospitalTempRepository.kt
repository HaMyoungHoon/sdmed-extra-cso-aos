package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository
import sdmed.extra.cso.interfaces.services.IHospitalTempService
import sdmed.extra.cso.utils.FExtensions

class HospitalTempRepository(private val _service: IHospitalTempService): IHospitalTempRepository {
    override suspend fun getData(thisPK: String) = FExtensions.restTryT { _service.getData(thisPK) }
    override suspend fun getListSearch(searchString: String) = FExtensions.restTryT { _service.getListSearch(searchString) }
    override suspend fun getListNearby(latitude: Double, longitude: Double, distance: Int) = FExtensions.restTryT { _service.getListNearby(latitude, longitude, distance) }
    override suspend fun getPharmacyListNearby(latitude: Double, longitude: Double, distance: Int) = FExtensions.restTryT { _service.getPharmacyListNearby(latitude, longitude, distance) }
}
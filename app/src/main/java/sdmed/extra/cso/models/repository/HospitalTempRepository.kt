package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IHospitalTempRepository
import sdmed.extra.cso.interfaces.services.IHospitalTempService

class HospitalTempRepository(private val _service: IHospitalTempService): IHospitalTempRepository {
}
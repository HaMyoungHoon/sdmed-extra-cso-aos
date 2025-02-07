package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IEDIDueDateRepository
import sdmed.extra.cso.interfaces.services.IEDIDueDateService
import sdmed.extra.cso.utils.FExtensions

class EDIDueDateRepository(private val _service: IEDIDueDateService): IEDIDueDateRepository {
    override suspend fun getList(date: String, isYear: Boolean) = FExtensions.restTryT { _service.getList(date, isYear) }
    override suspend fun getListRange(startDate: String, endDate: String) = FExtensions.restTryT { _service.getListRange(startDate, endDate) }
}
package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IMedicinePriceListRepository
import sdmed.extra.cso.interfaces.services.IMedicinePriceListService
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel
import sdmed.extra.cso.utils.FExtensions

class MedicinePriceListRepository(private val _service: IMedicinePriceListService): IMedicinePriceListRepository{
    override suspend fun getList() = FExtensions.restTryT { _service.getList() }
}
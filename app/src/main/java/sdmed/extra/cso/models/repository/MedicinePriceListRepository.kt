package sdmed.extra.cso.models.repository

import sdmed.extra.cso.interfaces.repository.IMedicinePriceListRepository
import sdmed.extra.cso.interfaces.services.IMedicinePriceListService
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel
import sdmed.extra.cso.utils.FExtensions

class MedicinePriceListRepository(private val _service: IMedicinePriceListService): IMedicinePriceListRepository{
    override suspend fun getList(page: Int, size: Int) = FExtensions.restTryT { _service.getList(page, size) }
    override suspend fun getLike(searchString: String, page: Int, size: Int) = FExtensions.restTryT { _service.getLike(searchString, page, size) }
}
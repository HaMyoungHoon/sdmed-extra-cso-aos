package sdmed.extra.cso.interfaces.services

import retrofit2.http.GET
import retrofit2.http.Query
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.RestPage
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.medicines.MedicineModel

interface IMedicinePriceListService {
    @GET("${FConstants.REST_API_MEDICINE_PRICE_LIST}/list/paging")
    suspend fun getList(@Query("page") page: Int = 0, @Query("size") size: Int = 10): RestResultT<RestPage<MutableList<MedicineModel>>>
    @GET("${FConstants.REST_API_MEDICINE_PRICE_LIST}/like/paging")
    suspend fun getLike(@Query("searchString") searchString: String, @Query("page") page: Int = 0, @Query("size") size: Int = 10): RestResultT<RestPage<MutableList<MedicineModel>>>
}
package sdmed.extra.cso.models.common

import kotlinx.coroutines.flow.MutableStateFlow
import sdmed.extra.cso.bases.FDataModelClass
import sdmed.extra.cso.interfaces.IRestPage

data class PaginationModel(
    var pages: MutableStateFlow<MutableList<PageNumberModel>> = MutableStateFlow(mutableListOf<PageNumberModel>()),
    var first: MutableStateFlow<Boolean> = MutableStateFlow(false),
    var last: MutableStateFlow<Boolean> = MutableStateFlow(false),
    var empty: Boolean = false,
    var size: Int = 0,
    var number: Int = 0,
    var numberOfElements: Int = 0,
    var totalElements: Int = 0,
    var totalPages: Int = 0,
): FDataModelClass<PaginationModel.ClickEvent>() {
    val sizeString: String get() = "$size"
    val numberString: String get() = "$number"
    val numberOfElementsString: String get() = "$numberOfElements"
    val totalElementsString: String get() = "$totalElements"
    val totalPagesString: String get() = "$totalPages"

    fun <T> init(data: IRestPage<T>?): PaginationModel {
        data ?: return this
        first.value = data.first
        last.value = data.last
        empty = data.empty
        size = data.size
        number = data.number
        numberOfElements = data.numberOfElements
        totalElements = data.totalElements
        totalPages = data.totalPages
        val pageBuff = mutableListOf<PageNumberModel>()
        for (i in 0 until totalPages) {
            pageBuff.add(PageNumberModel().apply {
                pageNumber = i + 1
            })
        }
        pages.value = pageBuff
        return this
    }

    enum class ClickEvent(var index: Int) {
        FIRST(0),
        PREV(1),
        NEXT(2),
        LAST(3)
    }
}
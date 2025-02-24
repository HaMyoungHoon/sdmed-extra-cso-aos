package sdmed.extra.cso.models

import sdmed.extra.cso.interfaces.IRestPage

class RestPage<T>: IRestPage<T> {
    override var content: T? = null
    override var pageable: Pageable = Pageable()
    override var first: Boolean = false
    override var last: Boolean = false
    override var empty: Boolean = false
    override var size: Int = 0
    override var number: Int = 0
    override var numberOfElements: Int = 0
    override var totalElements: Int = 0
    override var totalPages: Int = 0
}
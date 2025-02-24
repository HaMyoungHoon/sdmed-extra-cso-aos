package sdmed.extra.cso.models

import sdmed.extra.cso.interfaces.IPageable

class Pageable: IPageable {
    override var pageNumber: Int = 0
    override var pageSize: Int = 0
}
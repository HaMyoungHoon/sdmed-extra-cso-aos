package sdmed.extra.cso.interfaces

import sdmed.extra.cso.models.Pageable

interface IRestPage<T> {
    var content: T?
    var pageable: Pageable
    var first: Boolean
    var last: Boolean
    var empty: Boolean
    var size: Int
    var number: Int
    var numberOfElements: Int
    var totalElements: Int
    var totalPages: Int
}
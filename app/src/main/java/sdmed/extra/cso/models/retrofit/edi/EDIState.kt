package sdmed.extra.cso.models.retrofit.edi

enum class EDIState(var index: Int) {
    None(0),
    OK(1),
    Reject(2),
    Pending(3),
    Partial(4),
}
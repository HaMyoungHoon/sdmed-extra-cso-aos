package sdmed.extra.cso.models

import sdmed.extra.cso.interfaces.IRestResult

class RestResultT<T>: IRestResult {
    override var result: Boolean? = null
    override var code: Int? = null
    override var msg: String? = null
    var data: T? = null

    fun emptyResult(): RestResultT<T> {
        this.result = true
        this.code = 0
        this.msg = ""
        return this
    }
    fun setFail(code: Int? = -1, msg: String? = "not defined error"): RestResultT<T> {
        this.result = false
        this.code = code
        this.msg = msg
        return this
    }
    fun setFail(data: IRestResult): RestResultT<T> {
        this.result = data.result
        this.code = data.code
        this.msg = data.msg
        return this
    }
}
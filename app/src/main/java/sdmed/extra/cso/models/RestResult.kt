package sdmed.extra.cso.models

import sdmed.extra.cso.interfaces.IRestResult

class RestResult: IRestResult {
    override var result: Boolean? = null
    override var code: Int? = null
    override var msg: String? = null

    fun setFail(code: Int? = -1, msg: String? = "not defined error"): RestResult {
        this.result = false
        this.code = code
        this.msg = msg
        return this
    }
    fun setFail(data: IRestResult): RestResult {
        this.result = data.result
        this.code = data.code
        this.msg = data.msg
        return this
    }
}
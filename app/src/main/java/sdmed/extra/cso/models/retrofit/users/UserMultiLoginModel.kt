package sdmed.extra.cso.models.retrofit.users

import sdmed.extra.cso.bases.FDataModelClass

data class UserMultiLoginModel(
    var thisPK: String = "",
    var id: String = "",
    var name: String = "",
    var token: String = "",
    var isLogin: Boolean = false,
): FDataModelClass<UserMultiLoginModel.ClickEvent>() {
    fun safeCopy(rhs: UserMultiLoginModel): UserMultiLoginModel {
        this.thisPK = rhs.thisPK
        this.id = rhs.id
        this.name = rhs.name
        this.token = rhs.token
        this.isLogin = rhs.isLogin
        return this
    }

    enum class ClickEvent(var index: Int) {
        THIS(0)
    }
}
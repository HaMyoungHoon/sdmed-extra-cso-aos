package sdmed.extra.cso.bases

import sdmed.extra.cso.interfaces.command.ICommand

abstract class FDataModelClass<T: Enum<T>> {
    @Transient
    var relayCommand: ICommand? = null
    open fun onClick(eventName: T) {
        relayCommand?.execute(arrayListOf(eventName, this))
    }
    open fun onLongClick(eventName: T): Boolean {
        relayCommand?.execute(arrayListOf(eventName, this))
        return true
    }
}
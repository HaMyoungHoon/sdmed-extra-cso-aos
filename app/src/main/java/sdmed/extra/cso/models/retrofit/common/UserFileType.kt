package sdmed.extra.cso.models.retrofit.common

enum class UserFileType(var index: Int) {
    Taxpayer(1),
    BankAccount(2),
    CsoReport(3),
    MarketingContract(4);
    companion object {
        fun parseIndex(index: Int?) = entries.find { x -> x.index == index } ?: Taxpayer
    }
}
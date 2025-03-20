package sdmed.extra.cso.models.retrofit.users

enum class UserFileType(var index: Int) {
    Taxpayer(1),
    BankAccount(2),
    CsoReport(3),
    MarketingContract(4);
    companion object {
        fun parseIndex(index: Int?) = UserFileType.entries.find { x -> x.index == index } ?: UserFileType.Taxpayer
    }
}
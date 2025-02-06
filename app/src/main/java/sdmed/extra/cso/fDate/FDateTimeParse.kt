package sdmed.extra.cso.fDate

object FDateTimeParse {
    // 일단 yyyy-MM-dd 한정
    fun parse(data: String, localize: FLocalize): FDateTime {
        val fDateTime2 = FDateTime2().setThis(data)
        return FDateTime().setThis(fDateTime2.getYear(), fDateTime2.getMonth(), fDateTime2.getDay())
    }
}
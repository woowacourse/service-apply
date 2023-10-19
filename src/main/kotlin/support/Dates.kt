package support

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun createLocalDateTime(
    year: Int,
    month: Int = 1,
    dayOfMonth: Int = 1,
    hour: Int = 0,
    minute: Int = 0,
    second: Int = 0,
    nanoOfSecond: Int = 0
): LocalDateTime {
    return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond)
}

fun createLocalDate(
    year: Int,
    month: Int = 1,
    dayOfMonth: Int = 1
): LocalDate {
    return LocalDate.of(year, month, dayOfMonth)
}

fun LocalDateTime.nextMinutes(standard: Long): LocalDateTime {
    return this.truncatedTo(ChronoUnit.MINUTES).plusMinutes((standard - (this.minute % standard)))
}

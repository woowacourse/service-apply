package support

import java.time.LocalDateTime

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

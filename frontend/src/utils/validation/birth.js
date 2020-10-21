const YEAR_REGEX = /^(19|20)\d{2}$/
const MONTH_REGEX = /^(0?[1-9]|1[012])$/
const DAY_REGEX = /^(0?[1-9]|[12][0-9]|3[0-1])$/

export const isYearValid = v => YEAR_REGEX.test(v)
export const isMonthValid = v => MONTH_REGEX.test(v)
export const isDayValid = v => DAY_REGEX.test(v)

export const YEAR_MESSAGE = "태어난 년도 4자리를 정확하게 입력해 주세요."
export const MONTH_MESSAGE = "태어난 월을 정확하게 입력해 주세요."
export const DAY_MESSAGE = "태어난 일을 정확하게 입력해 주세요."

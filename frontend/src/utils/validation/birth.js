import { common } from "./common"

const YEAR_REGEX = /^(19|20)\d{2}$/
const MONTH_REGEX = /^(0?[1-9]|1[012])$/
const DAY_REGEX = /^(0?[1-9]|[12][0-9]|3[0-1])$/

export const birth = {
  year: [...common.required, v => YEAR_REGEX.test(v) || "태어난 년도를 정확하게 입력해 주세요."],
  month: [...common.required, v => MONTH_REGEX.test(v) || "태어난 월을 정확하게 입력해 주세요."],
  day: [...common.required, v => DAY_REGEX.test(v) || "태어난 일을 정확하게 입력해 주세요."],
}

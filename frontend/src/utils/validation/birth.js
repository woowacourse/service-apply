import { common } from "./common"

export const birth = {
  year: [...common.required, v => v.length === 4 || "태어난 년도를 4자리로 입력해 주세요."],
  month: [...common.required, v => v.length === 2 || "태어난 월을 2자리로 입력해 주세요."],
  day: [...common.required, v => v.length === 2 || "태어난 일을 2자리로 입력해 주세요."],
}

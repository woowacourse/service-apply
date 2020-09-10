import { common } from "./common"

const REGEX = /^[가-힣]+$/

export const name = [...common.required, v => REGEX.test(v) || "정확한 이름을 입력해 주세요"]

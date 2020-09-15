import { common } from "./common"

const REGEX = /^\d{2,3}-\d{3,4}-\d{4}$/

export const phoneNumber = [...common.required, v => REGEX.test(v) || "정확한 전화번호를 입력해 주세요. ex) 010-1234-5678"]

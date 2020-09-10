import { common } from "./common"

const REGEX = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i

export const email = [...common.required, v => REGEX.test(v) || "정확한 이메일 양식으로 채워주세요"]

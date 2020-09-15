import { common } from "./common"
import { name } from "./name"
import { email } from "./email"
import { phoneNumber } from "./phoneNumber"

export const register = {
  name: [...name],
  phoneNumber: [...phoneNumber],
  email: [...email],
  password: [...common.required],
  rePassword: [...common.required],
  recruitmentItem: [...common.required],
}

import { common } from "./common"
import { name } from "./name"
import { email } from "./email"
import { phoneNumber } from "./phoneNumber"
import { password } from "./password"

export const register = {
  name: [...name],
  phoneNumber: [...phoneNumber],
  email: [...email],
  password: [...password],
  rePassword: [...password],
  recruitmentItem: [...common.required],
}

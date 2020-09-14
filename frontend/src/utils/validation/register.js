import { common } from "./common"
import { name } from "./name"
import { email } from "./email"

export const register = {
  name: [...name],
  phoneNumber: [...common.required],
  email: [...email],
  password: [...common.required],
  rePassword: [...common.required],
  recruitmentItem: [...common.required],
}

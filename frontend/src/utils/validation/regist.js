import { common } from "./common"

export const regist = {
  name: [...common.required],
  phoneNumber: [...common.required],
  email: [...common.required],
  password: [...common.required],
  rePassword: [...common.required],
  recruitmentItem: [...common.required],
}

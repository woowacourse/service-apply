import { common } from "./common"
import { name } from "./name"
import { email } from "./email"

export const login = {
  name: [...name],
  email: [...email],
  password: [...common.required],
}

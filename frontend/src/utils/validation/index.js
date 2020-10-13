import Vue from "vue"
import { ValidationProvider, extend, ValidationObserver } from "vee-validate"
import * as name from "./name"
import * as email from "./email"
import * as required from "./reqruied"
import * as birth from "./birth"
import * as password from "./password"
import * as phoneNumber from "./phoneNumber"
import * as url from "./url"

extend("required", {
  validate: v => {
    return {
      require: true,
      valid: required.isValid(v),
    }
  },
  message: required.MESSAGE,
  computesRequired: true,
})

extend("url", {
  validate: v => url.isValid(v),
  message: url.MESSAGE,
})

extend("name", {
  validate: v => name.isValid(v),
  message: name.MESSAGE,
})

extend("year", {
  validate: v => birth.isYearValid(v.year),
  message: birth.YEAR_MESSAGE,
})

extend("month", {
  validate: v => birth.isMonthValid(v.month),
  message: birth.MONTH_MESSAGE,
})

extend("day", {
  validate: v => birth.isDayValid(v.day),
  message: birth.DAY_MESSAGE,
})

extend("email", {
  validate: v => email.isValid(v),
  message: email.MESSAGE,
})

extend("password", {
  validate: v => password.isValid(v),
  message: password.MESSAGE,
})

extend("rePassword", {
  params: ["target"],
  validate: (v, { target }) => v === target,
  message: "비밀번호가 일치하지 않습니다.",
})

extend("phoneNumber", {
  validate: v => phoneNumber.isValid(v),
  message: phoneNumber.MESSAGE,
})

Vue.component("ValidationProvider", ValidationProvider)
Vue.component("ValidationObserver", ValidationObserver)

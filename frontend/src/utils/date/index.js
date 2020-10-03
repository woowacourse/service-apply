import Vue from "vue"

Vue.use(require("vue-moment"))

export const formatLocalDate = ({ year, month, day }) => {
  const localDateTime = new Date(year, month - 1, day)

  return Vue.moment(localDateTime).format("YYYY-MM-DD")
}

export const parseLocalDateTime = localDateTime => {
  return Vue.moment(localDateTime).format("YYYY.MM.DD hh:mm:ss")
}

export const canSubmitToday = (start, end) => {
  return Vue.moment().isBetween(start, end)
}

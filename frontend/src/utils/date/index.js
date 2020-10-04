import dayjs from "dayjs"
import isBetween from "dayjs/plugin/isBetween"

dayjs.extend(isBetween)

export const formatLocalDate = ({ year, month, day }) => {
  const localDateTime = new Date(year, month - 1, day)

  return dayjs(localDateTime).format("YYYY-MM-DD")
}

export const parseLocalDateTime = localDateTime => {
  return dayjs(localDateTime).format("YYYY.MM.DD hh:mm:ss")
}

export const canSubmitToday = (start, end) => {
  return dayjs().isBetween(start, end)
}

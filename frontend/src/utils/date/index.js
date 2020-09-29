export const formatLocalDate = ({ year, month, day }) => {
  const monthFormatted = addZeroPrefixIfLessThanTen(month)
  const dayFormatted = addZeroPrefixIfLessThanTen(day)
  return `${year}-${monthFormatted}-${dayFormatted}`
}

export const parseLocalDateTime = localDateTime => {
  const year = localDateTime.getFullYear().toString()
  const month = addZeroPrefixIfLessThanTen((localDateTime.getMonth() + 1).toString())
  const date = addZeroPrefixIfLessThanTen(localDateTime.getDate().toString())
  const hour = addZeroPrefixIfLessThanTen(localDateTime.getHours().toString())
  const minute = addZeroPrefixIfLessThanTen(localDateTime.getMinutes().toString())
  const second = addZeroPrefixIfLessThanTen(localDateTime.getSeconds().toString())

  return `${year}.${month}.${date}
  ${hour}:${minute}:${second}`
}

const addZeroPrefixIfLessThanTen = value => value.padStart(2, "0")

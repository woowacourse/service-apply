export const formatLocalDate = (year, month, day) => {
  const monthFormatted = addZeroPrefixIfLessThanTen(month)
  const dayFormatted = addZeroPrefixIfLessThanTen(day)
  return `${year}-${monthFormatted}-${dayFormatted}`
}

const addZeroPrefixIfLessThanTen = value => {
  return value < 10 ? `0${value}` : `${value}`
}

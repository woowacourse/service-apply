export const formatLocalDate = (year, month, day) => {
  const monthFormatted = this.addZeroPrefixIfLessThanTen(month)
  const dayFormatted = this.addZeroPrefixIfLessThanTen(day)
  return `${year}-${monthFormatted}-${dayFormatted}`
}

export const addZeroPrefixIfLessThanTen = value => {
  return `${value < 10 ? `0${value}` : value}`
}

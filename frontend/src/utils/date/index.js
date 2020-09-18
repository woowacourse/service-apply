export const formatLocalDate = ({ year, month, day }) => {
  const monthFormatted = addZeroPrefixIfLessThanTen(month)
  const dayFormatted = addZeroPrefixIfLessThanTen(day)
  return `${year}-${monthFormatted}-${dayFormatted}`
}

const addZeroPrefixIfLessThanTen = value => value.padStart(2, "0")

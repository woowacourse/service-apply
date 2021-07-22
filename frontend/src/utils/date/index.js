export const formatLocalDate = ({ year, month, day }) =>
  `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;

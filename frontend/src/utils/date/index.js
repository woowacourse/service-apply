export const formatLocalDate = ({ year, month, day }) =>
  `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;

export const formatDateTime = (value) => {
  const year = value.getFullYear();
  const month = value.getMonth();
  const date = value.getDate();
  const hour = value.getHours();
  const minute = value.getMinutes();
  const second = value.getSeconds();

  if (
    isNaN(year) ||
    isNaN(month) ||
    isNaN(date) ||
    isNaN(hour) ||
    isNaN(minute) ||
    isNaN(second)
  )
    return value;

  return `${year}-${String(month).padStart(2, "0")}-${String(date).padStart(
    2,
    "0"
  )} ${String(hour).padStart(2, "0")}:${String(minute).padStart(
    2,
    "0"
  )}:${String(second).padStart(2, "0")}`;
};

export const formatLocalDate = ({ year, month, day }) =>
  `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;

export const formatDateTime = (value) => {
  const year = value.getFullYear();
  const month = value.getMonth();
  const date = value.getDate();
  const hour = value.getHours();
  const minute = value.getMinutes();

  if (isNaN(year) || isNaN(month) || isNaN(date) || isNaN(hour) || isNaN(minute)) return value;

  return `${year}-${String(month + 1).padStart(2, "0")}-${String(date).padStart(2, "0")} ${String(
    hour
  ).padStart(2, "0")}:${String(minute).padStart(2, "0")}`;
};

export const formatBirthday = ({ year, month, day }) =>
  `${year}-${String(month).padStart(2, "0")}-${String(day).padStart(2, "0")}T00:00:00`;

export const formatTimerText = (seconds) =>
  `${String(Math.floor(seconds / 60)).padStart(2, "0")}:${String(seconds % 60).padStart(2, "0")}`;

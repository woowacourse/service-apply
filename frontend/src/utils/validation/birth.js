const YEAR_REGEX = /^(19|20)\d{2}$/;
const MONTH_REGEX = /^(0?[1-9]|1[012])$/;
const DAY_REGEX = /^(0?[1-9]|[12][0-9]|3[0-1])$/;

export const isValidYear = (v) => YEAR_REGEX.test(v);
export const isValidMonth = (v) => MONTH_REGEX.test(v);
export const isValidDay = (v) => DAY_REGEX.test(v);

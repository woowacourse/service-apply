import { ERROR_MESSAGE } from '../../constants/messages';

const YEAR_REGEX = /^(19|20)\d{2}$/;
const MONTH_REGEX = /^(0?[1-9]|1[012])$/;
const DAY_REGEX = /^(0?[1-9]|[12][0-9]|3[0-1])$/;

const isValidYear = (v) => YEAR_REGEX.test(v);
const isValidMonth = (v) => MONTH_REGEX.test(v);
const isValidDay = (v) => DAY_REGEX.test(v);

export const validateYear = (v) => {
  if (!isValidYear(v)) throw new Error(ERROR_MESSAGE.VALIDATION.YEAR);
};

export const validateMonth = (v) => {
  if (!isValidMonth(v)) throw new Error(ERROR_MESSAGE.VALIDATION.MONTH);
};

export const validateDay = (v) => {
  if (!isValidDay(v)) throw new Error(ERROR_MESSAGE.VALIDATION.DAY);
};

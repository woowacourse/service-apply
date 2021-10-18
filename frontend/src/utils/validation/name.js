import { ERROR_MESSAGE } from '../../constants/messages';

const REGEX = /^[가-힣]+$/;

const isValidName = (v) => REGEX.test(v);

export const validateName = (v) => {
  if (!isValidName(v)) throw new Error(ERROR_MESSAGE.VALIDATION.NAME);
};

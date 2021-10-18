import { ERROR_MESSAGE } from "../../constants/messages";

const REGEX = /^[가-힣]+$/;

export const isValidName = (v) => REGEX.test(v);

// TODO: 제거
export const validateName = (v) => {
  if (!isValidName(v)) throw new Error(ERROR_MESSAGE.VALIDATION.NAME);
};

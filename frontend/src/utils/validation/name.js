import { ERROR_MESSAGE } from "../../constants/messages";

export const NAME_MAX_LENGTH = 255;
const REGEX = /^[가-힣]+$/;

const isExceedMaxLength = (v) => v.length > NAME_MAX_LENGTH;

const isValidName = (v) => REGEX.test(v);

export const validateName = (v) => {
  if (isExceedMaxLength(v)) {
    throw new Error(ERROR_MESSAGE.VALIDATION.NAME_LENGTH);
  }

  if (!isValidName(v)) {
    throw new Error(ERROR_MESSAGE.VALIDATION.NAME);
  }
};

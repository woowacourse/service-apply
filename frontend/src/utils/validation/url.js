import { ERROR_MESSAGE } from "../../constants/messages";

const REGEX =
  /^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)[a-z0-9]+([-.][a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;

const isValidURL = (v) => v === "" || REGEX.test(v);

export const validateURL = (v) => {
  if (!isValidURL(v)) throw new Error(ERROR_MESSAGE.VALIDATION.URL);
};

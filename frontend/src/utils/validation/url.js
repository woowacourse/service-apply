const REGEX =
  /^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)[a-z0-9]+([-.][a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;

export const isValid = (v) => v === "" || REGEX.test(v);

export const MESSAGE = "http(s)를 포함한 정확한 URL을 입력해 주세요.";

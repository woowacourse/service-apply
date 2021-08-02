const REGEX = /^[가-힣]+$/;

export const isValidName = (v) => REGEX.test(v);

export const NAME_ERROR_MESSAGE = "정확한 한글 이름을 입력해 주세요.";

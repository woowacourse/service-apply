const REGEX = /^([^ㄱ-ㅎㅏ-ㅣ가-힣])+$/;

export const isValidGithubUsername = (v) => REGEX.test(v);

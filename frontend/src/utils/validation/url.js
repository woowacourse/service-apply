const REGEX = /^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-z0-9]+([-.][a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/

export const isValid = v => v === "" || REGEX.test(v)

export const MESSAGE = "정확한 url을 입력해주세요."

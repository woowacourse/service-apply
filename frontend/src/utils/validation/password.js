const REGEX = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[*.!@#$%^&(){}[\]:;"'<>,?\\/~_+\-=|]).{8,20}$/

export const isValid = v => REGEX.test(v)

export const MESSAGE = "영문, 숫자, 특수문자 포함하여 8자에서 20자 이내로 적어주세요."

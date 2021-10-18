const REGEX = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[*.!@#$%^&(){}[\]:;"'<>,?\\/~_+\-=|]).{8,20}$/;

export const isValidPassword = (v) => REGEX.test(v);

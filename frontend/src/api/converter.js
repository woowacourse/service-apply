export const convert = ({success, message, body}) => success ? Promise.reject(message) : Promise.resolve(body)

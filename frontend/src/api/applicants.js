import axios from "axios"

const BASE_URL = "/api/applicants"

export const fetchRegister = ({name, phoneNumber, email, password, birthday, gender}) => {
    return axios.post(`${BASE_URL}/register`, {
        name,
        phoneNumber,
        email,
        password,
        birthday,
        gender,
    }).body
}
export const fetchLogin = ({name, email, birthday, password}) => {
    return axios.post(`${BASE_URL}/login`, {name, email, birthday, password}).body
}
export const fetchPasswordFind = ({name, email, birthday}) => {
    return axios.post("/api/applicants/reset-password", {name, email, birthday})
}

import axios from "axios"
import {convert} from "@/api/converter";

const BASE_URL = "/api/applicants"

export const fetchRegister = ({name, phoneNumber, email, password, birthday, gender}) => {
    return convert(axios.post(`${BASE_URL}/register`, {
        name,
        phoneNumber,
        email,
        password,
        birthday,
        gender,
    }).data)
}
export const fetchLogin = ({name, email, birthday, password}) => {
    return convert(axios.post(`${BASE_URL}/login`, {name, email, birthday, password}).data)
}
export const fetchPasswordFind = ({name, email, birthday}) => {
    return convert(axios.post("/api/applicants/reset-password", {name, email, birthday}).data)
}

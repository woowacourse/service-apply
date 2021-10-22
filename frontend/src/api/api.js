import axios from "axios";
import { LOCAL_STORAGE_KEY } from "../constants/key";
import { ERROR_MESSAGE } from "../constants/messages";
import PATH from "../constants/path";

axios.defaults.baseURL = process.env.REACT_APP_API_BASE_URL;

axios.interceptors.response.use(
  function (response) {
    return Promise.resolve({ data: response.data["body"] || {} });
  },
  function (error) {
    if (error.response?.status === 401) {
      alert(ERROR_MESSAGE.API.TOKEN_EXPIRED);
      localStorage.removeItem(LOCAL_STORAGE_KEY.ACCESS_TOKEN);
      document.location.href = PATH.LOGIN;

      return Promise.reject();
    }

    return Promise.reject(error);
  }
);

export const headers = ({ token }) => ({
  headers: {
    ...(token && { Authorization: `Bearer ${token}` }),
  },
});

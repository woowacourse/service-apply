import axios from "axios";

axios.defaults.baseURL = process.env.REACT_APP_API_BASE_URL;

axios.interceptors.response.use(
  function (response) {
    return Promise.resolve({ data: response.data["body"] || {} });
  },
  function (error) {
    return Promise.reject(error);
  }
);

export const headers = ({ token }) => ({
  headers: {
    ...(token && { Authorization: `Bearer ${token}` }),
  },
});

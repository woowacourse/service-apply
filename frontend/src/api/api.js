import axios from "axios";

axios.defaults.baseURL = process.env.API_BASE_URL;

axios.interceptors.response.use(
  function (response) {
    return Promise.resolve({ data: response.data["body"] || {} });
  },
  function (error) {
    return Promise.reject(error);
  }
);

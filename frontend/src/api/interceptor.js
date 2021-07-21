import axios from "axios";

axios.interceptors.response.use(
  function (response) {
    return Promise.resolve({ data: response.data["body"] || {} });
  },
  function (error) {
    return Promise.reject(error);
  }
);

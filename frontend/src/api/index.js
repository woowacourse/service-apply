import Vue from "vue"
import axios from "axios"
import VueAxios from "vue-axios"

const API_PREFIX = "/api"

const ApiService = {
  init() {
    Vue.use(VueAxios, axios)
  },
  get(uri) {
    return Vue.axios.get(API_PREFIX + `${uri}`, {
      headers: {
        Authorization: `${localStorage.getItem("token")}` || "",
      },
    })
  },
  getWithParams(uri, params) {
    return Vue.axios.get(API_PREFIX + `${uri}`, {
      headers: {
        Authorization: `${localStorage.getItem("token")}` || "",
      },
      params: params,
    })
  },
  login(uri, config) {
    return Vue.axios.post(API_PREFIX + `${uri}`, {}, config)
  },
  post(uri, params) {
    return Vue.axios.post(API_PREFIX + `${uri}`, params, {
      headers: {
        Authorization: `${localStorage.getItem("token")}` || "",
      },
    })
  },
  update(uri, params) {
    return Vue.axios.put(API_PREFIX + uri, params, {
      headers: {
        Authorization: `${localStorage.getItem("token")}` || "",
      },
    })
  },
  delete(uri) {
    return Vue.axios.delete(API_PREFIX + uri, {
      headers: {
        Authorization: `${localStorage.getItem("token")}` || "",
      },
    })
  },
}
ApiService.init()
export default ApiService

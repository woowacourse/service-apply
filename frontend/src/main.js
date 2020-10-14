import Vue from "vue"
import App from "./App.vue"
import router from "./router"
import store from "./store"
import axios from "axios"
import "./utils/validation"

axios.defaults.baseURL = process.env.VUE_APP_BACKEND_URL

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount("#app")

document.title = "우아한테크코스 지원하기"

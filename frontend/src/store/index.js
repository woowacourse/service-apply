import Vue from "vue"
import Vuex from "vuex"
import { token } from "@/store/token"
import { recruitments } from "@/store/recruitments"

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    recruitments: recruitments,
    token: token,
  },
})

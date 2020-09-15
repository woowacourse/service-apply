import Vue from "vue"
import Vuex from "vuex"
import * as Api from "@/api"

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: "",
  },
  mutations: {
    setToken(state, payload) {
      state.token = payload
    },
  },
  actions: {
    async fetchToken(
      { commit },
      { name, phoneNumber, email, password, birthday, gender, recruitmentId },
    ) {
      const { data: token } = await Api.fetchToken({
        name,
        phoneNumber,
        email,
        password,
        birthday,
        gender,
        recruitmentId,
      })

      commit("setToken", token)
    },
  },
  modules: {},
})

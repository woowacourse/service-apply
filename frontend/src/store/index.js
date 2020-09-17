import Vue from "vue"
import Vuex from "vuex"
import * as Api from "@/api"

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: "",
    applicantInfo: {
      name: "",
      phoneNumber: "",
      email: "",
      birthday: "",
      gender: "",
    },
  },
  mutations: {
    setToken(state, payload) {
      state.token = payload
    },
    setApplicantInfo(state, payload) {
      state.applicantInfo = payload
    },
  },
  actions: {
    async fetchTokenAndSetApplicantInfo(
      { commit },
      { name, phoneNumber, email, password, birthday, gender },
    ) {
      const { data: token } = await Api.fetchToken({
        name,
        phoneNumber,
        email,
        password,
        birthday,
        gender,
      })

      commit("setToken", token)
      commit("setApplicantInfo", { name, phoneNumber, email, birthday, gender })
    },
  },
  modules: {},
})

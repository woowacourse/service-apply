import Vue from "vue"
import Vuex from "vuex"
import * as Api from "@/api"
import { ApplicantInfo } from "@/store/ApplicantInfo"

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
  modules: {
    applicantInfo: ApplicantInfo,
  },
})

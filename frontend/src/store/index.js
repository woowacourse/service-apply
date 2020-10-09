import Vue from "vue"
import Vuex from "vuex"
import { applicantInfo } from "@/store/applicantInfo"
import { token } from "@/store/token"
import { recruitments } from "@/store/recruitments"

Vue.use(Vuex)

export default new Vuex.Store({
  state: {},
  mutations: {},
  actions: {
    async fetchRegisterAndSetApplicantInfo(
      { commit, dispatch },
      { name, phoneNumber, email, password, birthday, gender },
    ) {
      await Promise.all([
        dispatch("fetchRegister", { name, phoneNumber, email, password, birthday, gender }),
        commit("setApplicantInfo", { name, phoneNumber, email, birthday, gender }),
      ])
    },
  },
  modules: {
    applicantInfo: applicantInfo,
    recruitments: recruitments,
    token: token,
  },
})

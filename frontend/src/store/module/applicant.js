import * as api from "@/api/applicants"

export default {
  namespaced: true,
  state: {
    applicant: {
      token: "",
    },
  },
  mutations: {
    SET_APPLICANT(state, applicant) {
      state.applicant = applicant
    },
  },
  actions: {
    async login({ commit }, data) {
      const applicant = await api.fetchLogin(data)
      commit("SET_APPLICANT", applicant)
    },
  },
  getters: {
    getApplicant: state => {
      return state.applicant
    },
  },
}

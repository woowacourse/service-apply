import * as Api from "@/api"

export const token = {
  namespaced: true,
  state: () => ({ value: "" }),
  mutations: {
    setToken(state, token) {
      state.value = token
    },
  },
  actions: {
    async fetchRegister({ commit }, payload) {
      const { data: token } = await Api.fetchRegister(payload)
      commit("setToken", token)
    },

    async fetchLogin({ commit }, payload) {
      const { data: token } = await Api.fetchLogin(payload)
      commit("setToken", token)
    },

    resetToken({ commit }) {
      commit("setToken", "")
    },
  },
}

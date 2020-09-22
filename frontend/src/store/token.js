import * as Api from "@/api"

export const token = {
  state: () => ({ value: "" }),
  mutations: {
    setToken(state, token) {
      state.value = token
    },
  },
  actions: {
    async fetchToken({ commit }, { name, phoneNumber, email, password, birthday, gender }) {
      const { data: token } = await Api.fetchToken({
        name,
        phoneNumber,
        email,
        password,
        birthday,
        gender,
      })

      commit("setToken", token)
    },
  },
}

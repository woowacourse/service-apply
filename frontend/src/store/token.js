import * as Api from "@/api"

export const token = {
  state: () => ({ value: "" }),
  mutations: {
    setToken(state, token) {
      state.value = token
    },
  },
  actions: {
    async fetchRegister({ commit }, { name, phoneNumber, email, password, birthday, gender }) {
      const { data: token } = await Api.fetchRegister({
        name,
        phoneNumber,
        email,
        password,
        birthday,
        gender,
      })

      commit("setToken", token)
    },

    async login({ commit }, data) {
      const { data: token } = await Api.fetchLogin(data)

      commit("setToken", token)
    },
  },

  getters: {
    token: state => {
      return state.value
    },
  },
}

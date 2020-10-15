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

    async login({ commit }, { email, password }) {
      const { data: token } = await Api.fetchLogin({ email, password })

      commit("setToken", token)
    },

    resetToken({ commit }) {
      commit("setToken", "")
    },
  },

  getters: {
    token: state => {
      return state.value
    },
  },
}

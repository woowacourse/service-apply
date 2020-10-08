import * as Api from "@/api"

export const recruitments = {
  state: () => ({
    items: [],
  }),
  actions: {
    async fetchRecruitments({ commit }) {
      const { data: recruitments } = await Api.fetchRecruitments()

      commit(
        "setRecruitments",
        recruitments.sort((a, b) => b.id - a.id),
      )
    },
  },
  mutations: {
    setRecruitments(state, recruitments) {
      state.items = recruitments
    },
  },
}

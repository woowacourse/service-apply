import * as Api from "@/api"

export const recruitments = {
  state: () => ({
    recruitments: [],
  }),
  actions: {
    async fetchRecruitments({ commit }) {
      const { data: recruitments } = await Api.fetchRecruitments()

      await commit(
        "setRecruitments",
        recruitments.sort((a, b) => b.id - a.id),
      )
    },
  },
  mutations: {
    setRecruitments(state, recruitments) {
      state.recruitments = recruitments
    },
  },
  getters: {
    recruitments: state => {
      return state.recruitments
    },
  },
}

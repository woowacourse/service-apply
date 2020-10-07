import * as RecruitmentApi from "@/api/recruitments"

export const recruitments = {
  state: () => ({ recruitments: [] }),

  mutations: {
    setRecruitments(state, recruitments) {
      state.recruitments = recruitments
    },
  },

  actions: {
    async fetchAllRecruitments({ commit }) {
      const { data: recruitments } = await RecruitmentApi.fetchRecruitments()
      recruitments.sort((a, b) => b.id - a.id)
      commit("setRecruitments", recruitments)
    },
  },

  getters: {
    recruitments: state => {
      return state.recruitments
    },
  },
}

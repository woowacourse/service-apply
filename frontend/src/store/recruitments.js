import * as Api from "@/api"

export const recruitments = {
  namespaced: true,
  state: () => ({
    items: [],
    myApplicationForms: [],
  }),
  mutations: {
    setRecruitments(state, payload) {
      state.items = payload
    },
    setMyApplicationForms(state, payload) {
      state.myApplicationForms = payload
    },
  },
  actions: {
    async fetchRecruitments({ commit }) {
      const { data: recruitments } = await Api.fetchRecruitments()
      commit(
        "setRecruitments",
        recruitments.sort((a, b) => b.id - a.id),
      )
    },
    async fetchMyApplicationForms({ commit }, payload) {
      const { data: myApplicationForms } = await Api.fetchMyApplicationForms(payload)
      commit("setMyApplicationForms", myApplicationForms)
    },
  },
  getters: {
    all({ items }) {
      return items
    },
    recruitable({ items }) {
      return items.filter(({ status }) => status === "RECRUITABLE")
    },
    recruiting({ items }) {
      return items.filter(({ status }) => status === "RECRUITING" || status === "UNRECRUITABLE")
    },
    ended({ items }) {
      return items.filter(({ status }) => status === "ENDED")
    },
    applied({ items, myApplicationForms }) {
      return items
        .filter(recruitment =>
          myApplicationForms.find(form => form.recruitmentId === recruitment.id),
        )
        .map(recruitment => ({
          ...recruitment,
          submitted: !!myApplicationForms.find(form => form.recruitmentId === recruitment.id)
            .submitted,
        }))
    },
  },
}

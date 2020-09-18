export const ApplicantInfo = {
  state: () => ({
    name: "",
    phoneNumber: "",
    email: "",
    birthday: "",
    gender: "",
  }),
  mutations: {
    setApplicantInfo(state, { name, phoneNumber, email, birthday, gender }) {
      state.name = name
      state.phoneNumber = phoneNumber
      state.email = email
      state.birthday = birthday
      state.gender = gender
    },
  },
}

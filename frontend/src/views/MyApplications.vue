<template>
  <div class="my-application-forms">
    <Box>
      <h1>내 지원서</h1>
      <ApplicationFormItem
        class="application-forms"
        v-for="recruitment in appliedRecruitments"
        :key="recruitment.id"
        :recruitment="recruitment"
        :submitted="recruitment.submitted"
      />
    </Box>
  </div>
</template>

<script>
import * as ApplicationFormApi from "@/api/application-forms"
import ApplicationFormItem from "@/components/ApplicationFormItem"
import Box from "@/components/Box"

export default {
  components: {
    ApplicationFormItem,
    Box,
  },
  data: () => ({
    appliedRecruitments: [],
  }),
  async created() {
    const token = this.$store.getters["token"]

    try {
      const { data: myApplicationFormsData } = await ApplicationFormApi.fetchMyApplicationForms(
        token,
      )
      this.appliedRecruitments = this.findAppliedRecruitments(myApplicationFormsData)
    } catch (e) {
      alert("로그인이 필요합니다.")
      this.$router.replace("/login")
    }
  },
  methods: {
    findAppliedRecruitments(applicationFormsData) {
      return this.$store.state.recruitments.items
        .filter(recruitment =>
          applicationFormsData.find(form => form.recruitmentId === recruitment.id),
        )
        .map(recruitment => {
          return {
            ...recruitment,
            submitted: !!applicationFormsData.find(form => form.recruitmentId === recruitment.id)
              .submitted,
          }
        })
    },
  },
}
</script>

<style scoped>
.my-application-forms {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #ced6e0;
}
</style>

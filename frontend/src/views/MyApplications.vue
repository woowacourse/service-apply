<template>
  <div class="my-application-forms">
    <h1>내 지원서</h1>

    <div
      class="application-forms"
      v-for="(applicationForm, index) in applicationForms"
      :key="applicationForm.id"
    >
      <ApplicationFormItem
        :recruitment="recruitments[index]"
        :submitted="applicationForm.submitted"
      />
    </div>

    <footer>
      <a class="logo" href="#"></a>
    </footer>
  </div>
</template>

<script>
import * as RecruitmentApi from "../api/recruitments"
import * as ApplicationApi from "../api/applications"
import ApplicationFormItem from "@/components/ApplicationFormItem"

export default {
  components: {
    ApplicationFormItem,
  },
  data: () => ({
    applicationForms: [],
    recruitments: [],
  }),
  async mounted() {
    const token = this.$store.state.token.value

    try {
      const { applicationFormsData } = await ApplicationApi.fetchMyApplicationForms(token)
      const { recruitmentData } = await RecruitmentApi.fetchMyRecruitments(token)
      this.applicationForms = applicationFormsData
      this.recruitments = recruitmentData
    } catch (e) {
      alert("token이 유효하지 않습니다.")
      await this.$router.push("/login")
    }
  },
}
</script>

<style scoped>
.my-application-forms {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 100%;
  align-items: center;
  background: #ced6e0;
}

.application-forms {
  width: 60%;
}

.logo {
  display: flex;
  width: 100px;
  height: 32px;
  background: url("/assets/logo/logo_full_dark.png");
  background-size: 100% 100%;
}
</style>

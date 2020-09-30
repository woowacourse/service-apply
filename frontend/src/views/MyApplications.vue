<template>
  <div class="my-application-forms">
    <h1>내 지원서</h1>

    <ApplicationFormItem
      class="application-forms"
      v-for="recruitment in matchRecruitments"
      :key="recruitment.id"
      :recruitment="recruitment"
      :submitted="isSubmitted(recruitment.id)"
    />

    <footer>
      <a class="logo" href="#"></a>
    </footer>
  </div>
</template>

<script>
import * as RecruitmentApi from "../api/recruitments"
import * as ApplicationApi from "../api/application-forms"
import ApplicationFormItem from "@/components/ApplicationFormItem"

export default {
  components: {
    ApplicationFormItem,
  },
  data: () => ({
    applicationForms: [],
    recruitments: [],
  }),
  async created() {
    const token = this.$store.getters["token"]

    try {
      // todo: 백엔드 api에 따라 가능하면 한 번에 받아오도록 수정
      Promise.all([
        ApplicationApi.fetchMyApplicationForms(token),
        RecruitmentApi.fetchMyRecruitments(token),
      ]).then(values => {
        const { data: applicationFormsData } = values[0]
        const { data: recruitmentData } = values[1]
        this.applicationForms = applicationFormsData
        this.recruitments = recruitmentData
      })
    } catch (e) {
      alert("token이 유효하지 않습니다.")
      await this.$router.replace("/login")
    }
  },
  computed: {
    matchRecruitments() {
      if (
        !this.applicationForms ||
        !this.recruitments ||
        this.applicationForms.length !== this.recruitments.length
      ) {
        return []
      }

      return this.recruitments.filter(recruitment =>
        this.applicationForms.map(form => form.recruitmentId).includes(recruitment.id),
      )
    },
  },
  methods: {
    isSubmitted(recruitmentId) {
      return !!this.applicationForms.find(form => form.recruitmentId === recruitmentId).submitted
    },
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

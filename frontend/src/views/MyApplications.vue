<template>
  <div class="my-applications">
    <h1>내 지원서</h1>

    <div class="applications">
      <div v-for="(application, index) in applications" :key="application.id">
        <ApplicationItem
          :recruitment="recruitments[index]"
          :submitted="application.submitted"
        ></ApplicationItem>
      </div>
    </div>

    <footer>
      <a class="logo" href="#"></a>
    </footer>
  </div>
</template>

<script>
import * as RecruitmentApi from "../api/recruitments"
import * as ApplicationApi from "../api/applications"
import ApplicationItem from "@/components/ApplicationItem"

export default {
  components: {
    ApplicationItem,
  },
  data: () => ({
    applications: [],
    recruitments: [],
  }),
  async mounted() {
    const token = this.$store.state.token.value
    if (token === "") {
      alert("로그인이 필요합니다.")
      return this.$router.push("/login")
    }

    try {
      const { applicationsData } = await ApplicationApi.fetchMyApplications(token)
      const { recruitmentData } = await RecruitmentApi.fetchMyRecruitments(token)
      this.applications = applicationsData
      this.recruitments = recruitmentData
    } catch (e) {
      alert("token이 유효하지 않습니다.")
      await this.$router.push("/login")
    }
  },
}
</script>

<style scoped>
.my-applications {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 100%;
  align-items: center;
  background: #ced6e0;
}

.applications {
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

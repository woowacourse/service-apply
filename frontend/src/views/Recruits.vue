<template>
  <div class="recruits">
    <Box>
      <ul class="tab-list">
        <li v-for="tab in tabList" :key="tab.name" class="tab-item">
          <router-link
            :class="{ active: tab.name === selectedTab }"
            :to="{ path: '/recruits', query: { status: tab.name } }"
          >
            {{ tab.label }}
          </router-link>
        </li>
        <li v-if="token" class="tab-item edit-password">
          <router-link to="/edit">비밀번호 변경</router-link>
        </li>
      </ul>
      <div v-if="selectedTab !== 'applied'">
        <RecruitItem
          v-for="recruitment in this[selectedTab]"
          :key="recruitment.id"
          :recruitment="recruitment"
        />
      </div>
      <div v-else>
        <ApplicationFormItem
          class="application-forms"
          v-for="recruitment in this[selectedTab]"
          :key="recruitment.id"
          :recruitment="recruitment"
          :submitted="recruitment.submitted"
        />
      </div>
    </Box>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex"
import RecruitItem from "@/components/RecruitItem"
import ApplicationFormItem from "@/components/ApplicationFormItem"
import Box from "@/components/Box"

export default {
  name: "Recruits",
  components: {
    RecruitItem,
    ApplicationFormItem,
    Box,
  },
  data: () => ({
    tabList: [
      {
        name: "all",
        label: "전체",
      },
      {
        name: "recruitable",
        label: "모집 예정",
      },
      {
        name: "recruiting",
        label: "모집 중",
      },
      {
        name: "ended",
        label: "모집 종료",
      },
      {
        name: "applied",
        label: "내 지원서",
      },
    ],
  }),
  computed: {
    selectedTab() {
      return this.$route.query.status || "all"
    },
    token() {
      return this.$store.state.token.value
    },
    ...mapGetters("recruitments", ["all", "recruitable", "recruiting", "ended", "applied"]),
  },
  watch: {
    selectedTab: {
      async handler(newTab) {
        if (newTab !== "applied") {
          return
        }
        if (this.token === "") {
          alert("로그인이 필요합니다.")
          this.$router.replace("/login")
          return
        }
        try {
          await this.fetchMyApplicationForms(this.token)
        } catch (e) {
          alert("내 지원서를 불러오는데 실패했습니다.")
          this.$router.replace("/login")
        }
      },
      immediate: true,
    },
  },
  methods: {
    ...mapActions("recruitments", ["fetchMyApplicationForms"]),
  },
}
</script>

<style scoped>
.recruits {
  display: flex;
  flex-direction: column;
  align-items: center;
  user-select: none;
}

.tab-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 10px;
  overflow-x: auto;
  white-space: nowrap;
}

.tab-item {
  margin-right: 15px;
  font-weight: bold;
}

.tab-item.active {
  color: #333;
}

.tab-item a {
  text-decoration: none;
  color: #aaa;
}

.tab-item a.active {
  color: #333;
}

.tab-item.edit-password {
  display: flex;
  justify-content: flex-end;
  flex: 1;
  margin: 0;
}
</style>

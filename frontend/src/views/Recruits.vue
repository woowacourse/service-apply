<template>
  <div class="recruits">
    <Box>
      <ul class="tab-list">
        <li v-for="tab in tabList" :key="tab.name" class="tab-item">
          <router-link
            :class="{ active: tab.name === ($route.query.status || '') }"
            :to="{ path: '/recruits', [tab.name && 'query']: { status: tab.name } }"
          >
            {{ tab.label }}
          </router-link>
        </li>
        <li class="tab-item">
          <router-link to="/my-application-forms">내 지원서</router-link>
        </li>
      </ul>
      <RecruitItem
        v-for="recruitment in filteredRecruitments"
        :key="recruitment.id"
        :recruitment="recruitment"
      />
    </Box>
  </div>
</template>

<script>
import RecruitItem from "@/components/RecruitItem"
import Box from "@/components/Box"

export default {
  name: "Recruits",
  components: {
    RecruitItem,
    Box,
  },
  data: () => ({
    tabList: [
      {
        name: "",
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
    ],
  }),
  computed: {
    filteredRecruitments() {
      switch (this.$route.query.status) {
        case "recruitable":
          return this.recruitments.filter(({ status }) => status === "RECRUITABLE")
        case "recruiting":
          return this.recruitments.filter(
            ({ status }) => status === "RECRUITING" || status === "UNRECRUITABLE",
          )
        case "ended":
          return this.recruitments.filter(({ status }) => status === "ENDED")
      }
      return this.recruitments
    },
    recruitments() {
      return this.$store.state.recruitments.items
    },
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
</style>

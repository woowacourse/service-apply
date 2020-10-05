<template>
  <div class="recruits">
    <div class="recruits-box">
      <ul class="tab-list">
        <li v-for="tab in tabList" :key="tab.name" class="tab-item">
          <router-link
            :class="{ active: tab.name == ($route.query.status || '') }"
            :to="{ path: '/recruits', [tab.name && 'query']: { status: tab.name } }"
            >{{ tab.label }}</router-link
          >
        </li>
        <li class="tab-item">
          <router-link to="/my-applications">내 지원서</router-link>
        </li>
      </ul>
      <RecruitItem
        v-for="recruitment in filteredRecruits"
        :key="recruitment.id"
        :recruitment="recruitment"
      />
    </div>
  </div>
</template>

<script>
import * as RecruitmentApi from "@/api/recruitments"
import RecruitItem from "@/components/RecruitItem"

export default {
  name: "Recruits",
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
    recruits: [],
  }),
  components: {
    RecruitItem,
  },
  computed: {
    filteredRecruits() {
      switch (this.$route.query.status) {
        case "recruitable": {
          return this.recruits.filter(recruit => recruit.status === "RECRUITABLE")
        }
        case "recruiting": {
          return this.recruits.filter(
            recruit => recruit.status === "RECRUITING" || recruit.status === "UNRECRUITABLE",
          )
        }
        case "ended": {
          return this.recruits.filter(recruit => recruit.status === "ENDED")
        }
        default: {
          return this.recruits
        }
      }
    },
  },
  async created() {
    this.recruits = (await RecruitmentApi.fetchRecruitments()).data.sort((a, b) => b.id - a.id)
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

.recruits-box {
  width: 800px;
  max-width: 800px;
  padding: 20px;
  margin: 15px;
  border-radius: 3px;
  background: #f1f2f6;
  box-shadow: 0 0 7px rgba(0, 0, 0, 0.05);
}

@media (max-width: 800px) {
  .recruits-box {
    width: 100%;
    margin: 0;
  }
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

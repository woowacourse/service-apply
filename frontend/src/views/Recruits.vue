<template>
  <div id="wrapper">
    <div id="tab-wrapper">
      <h2
        v-for="tab in tabList"
        :key="tab.name"
        class="list-tab filter"
        :class="{ active: tab.name === $route.query.query }"
        :id="tab.name"
        @click="setFilter(tab.name)"
      >
        {{ tab.label }}
      </h2>
      <h2 class="list-tab" id="mypage">내 지원서</h2>
    </div>
    <div id="component">
      <RecruitListObject v-bind:recruitsList="activeList" />
    </div>
  </div>
</template>

<script>
import RecruitListObject from "@/components/RecruitListObject.vue"

export default {
  name: "Recruits",
  data() {
    return {
      filterBy: "all",
      tabList: [
        { name: "all", label: "전체" },
        { name: "recruiting", label: "모집 중" },
        { name: "completed", label: "모집 종료" },
      ],
      activeList: [],
      allList: [
        {
          id: 6,
          title: "웹 백엔드 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
        },
        {
          id: 5,
          title: "웹 프론트엔드 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
        },
        {
          id: 4,
          title: "모바일(iOS) 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
        },
        {
          id: 3,
          title: "모바일(Android) 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
        },
        {
          id: 2,
          title: "웹 백엔드 2기",
          startTime: new Date("2019-10-24T15:00:00"),
          endTime: new Date("2019-11-09T23:59:00"),
        },
        {
          id: 1,
          title: "웹 백엔드 1기",
          startTime: new Date("2019-01-24T15:00:00"),
          endTime: new Date("2020-02-09T23:59:00"),
        },
      ],
      recruitingList: [
        {
          id: 6,
          title: "웹 백엔드 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
        },
        {
          id: 5,
          title: "웹 프론트엔드 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
        },
        {
          id: 4,
          title: "모바일(iOS) 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
        },
        {
          id: 3,
          title: "모바일(Android) 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
        },
      ],
      completedList: [
        {
          id: 2,
          title: "웹 백엔드 2기",
          startTime: new Date("2019-10-24T15:00:00"),
          endTime: new Date("2019-11-09T23:59:00"),
        },
        {
          id: 1,
          title: "웹 백엔드 1기",
          startTime: new Date("2019-01-24T15:00:00"),
          endTime: new Date("2020-02-09T23:59:00"),
        },
      ],
    }
  },
  components: {
    RecruitListObject,
  },
  mounted() {
    this.loadByFilter()
  },
  methods: {
    setFilter(filter) {
      this.$router.push({
        path: "/recruits/?query=" + filter,
      })
      this.loadByFilter()
    },
    loadByFilter() {
      switch (this.$route.query.query) {
        case "recruiting": {
          this.activeList = this.recruitingList.slice()
          break
        }
        case "completed": {
          this.activeList = this.completedList.slice()
          break
        }
        case "all": {
          this.activeList = this.allList.slice()
          break
        }
        default: {
          this.setFilter("all")
          break
        }
      }
    },
  },
}
</script>

<style scoped>
@media (min-width: 800px) {
  #wrapper {
    width: 800px;
    margin: 0 auto;
  }
}

#tab-wrapper {
  display: flex;
}

.list-tab {
  padding: 0 20px 0 20px;
}

.filter {
  color: #aaaaaa;
}

.active {
  color: #000000 !important;
}

@media (max-width: 500px) {
  .list-tab {
    font-size: smaller;
    padding: 0 10px 0 10px;
  }
}
</style>

<template>
  <div id="wrapper">
    <div id="tab-wrapper">
      <h2 class="list-tab" id="all" @click="setFilter('all')">전체</h2>
      <h2 class="list-tab" id="ongoing" @click="setFilter('ongoing')">모집 중</h2>
      <h2 class="list-tab" id="finished" @click="setFilter('finished')">모집 종료</h2>
      <h2 class="list-tab" id="mypage">내 지원서</h2>
    </div>
    <div id="component">
      <RecruitListObject v-bind:recruitsList="recruitsList" />
    </div>
  </div>
</template>

<script>
import RecruitListObject from "@/components/RecruitListObject.vue"

export default {
  name: "Recruits",
  data() {
    return {
      recruitsList: [],
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
      ongoingList: [
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
      finishedList: [
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
    this.setFilter("all")
  },
  methods: {
    setFilter(param) {
      if (param === "all") {
        this.recruitsList = this.allList.slice()
      }
      if (param === "ongoing") {
        this.recruitsList = this.ongoingList.slice()
      }
      if (param === "finished") {
        this.recruitsList = this.finishedList.slice()
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

@media (max-width: 500px) {
  .list-tab {
    font-size: smaller;
    padding: 0 10px 0 10px;
  }
}
</style>

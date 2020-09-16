<template>
  <div id="recruits">
    <div id="recruits-box">
      <div id="tab-wrapper">
        <h2
          v-for="tab in tabList"
          :key="tab.name"
          class="list-tab filter"
          :class="{ active: tab.name === $route.query.status }"
          :id="tab.name"
          @click="setFilter(tab.name)"
        >
          {{ tab.label }}
        </h2>
        <h2 class="list-tab" id="mypage">내 지원서</h2>
      </div>
      <div id="component">
        <div v-for="recruitment in activeList" :key="recruitment.id">
          <recruit-item :recruitment="recruitment" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import RecruitItem from "@/components/RecruitItem.vue"

export default {
  name: "Recruits",
  data() {
    return {
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
      ],
      activeList: [],
      allList: [
        {
          id: 6,
          title: "웹 백엔드 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
          recruitmentStatus: "RECRUITING",
        },
        {
          id: 5,
          title: "웹 프론트엔드 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
          recruitmentStatus: "RECRUITING",
        },
        {
          id: 4,
          title: "모바일(iOS) 3기",
          startTime: new Date("2020-10-27T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
          recruitmentStatus: "RECRUITABLE",
        },
        {
          id: 3,
          title: "모바일(Android) 3기",
          startTime: new Date("2020-10-24T15:00:00"),
          endTime: new Date("2020-11-09T23:59:00"),
          recruitmentStatus: "UNRECRUITABLE",
        },
        {
          id: 2,
          title: "웹 백엔드 2기",
          startTime: new Date("2019-10-24T15:00:00"),
          endTime: new Date("2019-11-09T23:59:00"),
          recruitmentStatus: "ENDED",
        },
        {
          id: 1,
          title: "웹 백엔드 1기",
          startTime: new Date("2019-01-24T15:00:00"),
          endTime: new Date("2020-02-09T23:59:00"),
          recruitmentStatus: "ENDED",
        },
      ],
    }
  },
  components: {
    RecruitItem,
  },
  mounted() {
    this.setList(this.$route.query.status)
  },
  methods: {
    setFilter(filter) {
      this.$router.replace({
        path: "/recruits/?status=" + filter,
      })
      this.setList(filter)
    },
    async setList(filter) {
      try {
        this.activeList = await this.getRecruits(filter)
      } catch (e) {
        await this.setFilter("all")
      }
    },
    // TODO: 이 부분을 실제 API 콜로 대체하기
    getRecruits(filter) {
      switch (filter) {
        case "recruitable": {
          return this.allList.filter(el => el.recruitmentStatus === "RECRUITABLE")
        }
        case "recruiting": {
          return this.allList.filter(
            el => el.recruitmentStatus === "RECRUITING" || el.recruitmentStatus === "UNRECRUITABLE",
          )
        }
        case "ended": {
          return this.allList.filter(el => el.recruitmentStatus === "ENDED")
        }
        case "all": {
          return this.allList.slice()
        }
        default: {
          throw "잘못된 분류 기준입니다!"
        }
      }
    },
  },
}
</script>

<style scoped>
#recruits {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #ced6e0;
  height: 100%;
  user-select: none;
}

#recruits-box {
  align-items: center;
  width: 100%;
  max-width: 800px;
  padding: 20px;
  margin: 15px;
  border-radius: 3px;
  background: #f1f2f6;
  box-shadow: 0 0 7px rgba(0, 0, 0, 0.05);
}

@media (min-width: 800px) {
  #recruits-box {
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

.filter {
  color: #aaaaaa;
}

.active {
  color: #000000 !important;
}

#tab-wrapper > h2 {
  cursor: pointer;
}
.enroll-button button {
  cursor: pointer;
}
</style>

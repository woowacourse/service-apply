<template>
  <div>
    <div v-for="recruitment in recruitsList" class="card" :key="recruitment.id">
      <div class="list-wrapper">
        <div class="flex space-between">
          <div>
            <div class="recruit-title">
              <b>{{ recruitment.title }}</b>
            </div>
            <div class="recruit-duration">
              {{ parseTime(recruitment.startTime) }} ~ {{ parseTime(recruitment.endTime) }}
            </div>
          </div>
          <div class="button-wrapper">
            <button class="enroll-button" v-on:click="onClickAdmission(recruitment.id)">
              지원하기
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    recruitsList: {
      type: Array,
      required: true,
    },
  },
  methods: {
    onClickAdmission(id) {
      this.$router.push({
        path: "/application/" + id,
      })
    },
    parseTime(time) {
      return (
        time.getFullYear() +
        "." +
        (time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) : time.getMonth() + 1) +
        "." +
        (time.getDate() < 10 ? "0" + time.getDate() : time.getDate()) +
        " " +
        time.getHours() +
        ":" +
        (time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes())
      )
    },
  },
}
</script>

<style>
@media (max-width: 440px) {
  .card {
    height: 90px !important;
  }
  .button-wrapper {
    line-height: 90px !important;
  }
  .list-wrapper {
    height: 90px !important;
  }
}

.card {
  width: 100%;
  height: 70px;
  background-color: #dcdcdc;
  border-radius: 5px;
  display: inline-block;
  margin: 5px 0 5px 0;
}

.recruit-title {
  padding: 10px 0px 5px 10px;
  font-size: large;
}

.recruit-duration {
  padding: 5px 0px 10px 10px;
}

.flex {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: center;
}

.list-wrapper {
  display: flex;
  height: 70px;
}

.space-between {
  justify-content: space-between;
}

.enroll-button {
  width: 120px;
  height: 40px;
  margin: 10px;
  vertical-align: middle;
  background-color: #3498db;
}
</style>

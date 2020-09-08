<template>
  <div>
    <div v-for="card in recruitsList" class="card" :key="card.id">
      <div class="flex">
        <div>
          <div class="recruit-title">
            <b>{{ card.title }}</b>
          </div>
          <div class="recruit-duration">
            {{ parseTime(card.startTime) }} ~ {{ parseTime(card.endTime) }}
          </div>
        </div>
        <div>
          <button class="enroll-button">지원하기</button>
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
    parseTime(time) {
      return (
        time.getFullYear() +
        "." +
        (time.getMonth() < 10 ? "0" + time.getMonth() : time.getMonth()) +
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
.card {
  width: 100%;
  height: 80px;
  background-color: #dcdcdc;
  border-radius: 10px;
  display: inline-block;
  margin: 10px 0 10px 0;
}

.recruit-title {
  padding: 10px;
  font-size: large;
}

.recruit-duration {
  padding: 10px;
}

.flex {
  display: flex;
}

.enroll-button {
  width: 120px;
  height: 40px;
  right: auto;
}
</style>

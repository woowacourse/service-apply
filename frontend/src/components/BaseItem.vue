<template>
  <div class="base-item">
    <div class="title">{{ title }}</div>
    <div class="period">
      <span class="ti-calendar"></span>
      <div class="date">
        <span class="start">{{ startDateTime }}</span>
        <span class="between"></span>
        <span calss="end">{{ endDateTime }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    title: String,
    startDateTime: String,
    endDateTime: String,
  },
}
</script>

<style scoped>
.base-item {
  margin-right: 10px;
}

.title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 5px;
}

.period {
  display: flex;
  align-items: center;
  font-size: 16px;
  color: #333;
  border-radius: 5px;
  text-align: center;
}

.date {
  display: flex;
}

.start {
  padding-left: 5px;
}

.between {
  display: flex;
  align-items: center;
}

.between::before {
  content: "~";
  padding: 0 5px;
}
</style>

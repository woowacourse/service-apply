<template>
  <div class="common-item">
    <div class="information">
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
    <Button class="button" @click="$emit('click')" :disabled="!activeButton" :value="buttonLabel" />
  </div>
</template>

<script>
import Button from "@/components/form/Button"

export default {
  components: {
    Button,
  },
  props: {
    title: String,
    startDateTime: String,
    endDateTime: String,
    buttonLabel: String,
    activeButton: Boolean,
  },
}
</script>

<style scoped>
.common-item {
  display: flex;
  width: 100%;
  height: 70px;
  justify-content: space-between;
  align-items: center;
  background-color: #ffffff;
  border-radius: 3px;
  margin: 5px 0;
  box-sizing: border-box;
  padding: 10px;
  box-shadow: 0 0 7px rgba(0, 0, 0, 0.05);
  color: #333;
}

.information {
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
  font-size: 12px;
  background: #ecf0f1;
  color: #333;
  padding: 1px 5px;
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

.button {
  width: 110px;
  height: 40px;
}
</style>

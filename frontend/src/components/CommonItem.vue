<template>
  <div class="common-item">
    <div class="information">
      <div class="title">{{ title }}</div>
      <div class="period">{{ startDateTime }} ~ {{ endDateTime }}</div>
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

.title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 5px;
}

.period {
  font-size: 16px;
}

.button {
  width: 120px;
  height: 40px;
}
</style>

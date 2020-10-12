<template>
  <div class="common-item">
    <BaseItem
      :title="recruitment.title"
      :start-date-time="recruitment.startDateTime"
      :end-date-time="recruitment.endDateTime"
    />
    <Button class="button" @click="$emit('click')" :disabled="!activeButton" :value="buttonLabel" />
  </div>
</template>

<script>
import BaseItem from "@/components/BaseItem"
import Button from "@/components/form/Button"

export default {
  components: {
    BaseItem,
    Button,
  },
  props: {
    recruitment: {
      type: Object,
      required: true,
    },
    buttonLabel: String,
    activeButton: Boolean,
  },
}
</script>

<style scoped>
.common-item {
  display: flex;
  width: 100%;
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

.button {
  width: 110px;
  height: 40px;
}
</style>

<template>
  <Box class="recruit-card">
    <BaseItem
      :title="recruitment.title"
      :start-date-time="recruitment.startDateTime"
      :end-date-time="recruitment.endDateTime"
    />
  </Box>
</template>

<script>
import Box from "@/components/Box"
import BaseItem from "@/components/BaseItem"

export default {
  components: {
    Box,
    BaseItem,
  },
  props: {
    recruitment: {
      type: Object,
      required: true,
    },
  },
}
</script>
<style scoped>
.recruit-card {
  width: 512px;
  margin-bottom: 0;
}

@media screen and (max-width: 512px) {
  .recruit-card {
    width: 100vw;
    box-shadow: none;
    border-radius: 0;
    border-bottom: 1px solid #ddd;
  }
}
</style>

<template>
  <Box class="recruit-card">
    <BaseItem
      :title="recruitment.title"
      :start-date-time="startDateTime"
      :end-date-time="endDateTime"
    />
  </Box>
</template>

<script>
import Box from "@/components/Box"
import BaseItem from "@/components/BaseItem"
import { parseLocalDateTime } from "@/utils/date"

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
  computed: {
    startDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.startDateTime))
    },
    endDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.endDateTime))
    },
  },
}
</script>
<style scoped>
.recruit-card {
  max-width: 512px;
  margin-bottom: 0;
}

@media screen and (max-width: 512px) {
  .recruit-card {
    box-shadow: none;
    border-radius: 0;
    border-bottom: 1px solid #ddd;
  }
}
</style>

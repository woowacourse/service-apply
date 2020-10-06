<template>
  <CommonItem
    :title="recruitment.title"
    :start-date-time="startDateTime"
    :end-date-time="endDateTime"
    :buttonLabel="buttonLabel"
    :activeButton="!submitted"
    @click="onClickAdmission(recruitment.id)"
  />
</template>

<script>
import CommonItem from "@/components/CommonItem"
import { parseLocalDateTime } from "@/utils/date"

export default {
  components: {
    CommonItem,
  },
  props: {
    recruitment: {
      type: Object,
      required: true,
    },
    submitted: {
      type: Boolean,
      required: true,
    },
  },
  computed: {
    buttonLabel() {
      return this.submitted ? "제출 완료" : "지원서 수정"
    },
    startDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.startDateTime))
    },
    endDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.endDateTime))
    },
  },
  methods: {
    onClickAdmission(id) {
      this.$router.push({ path: `/register/applicant/${id}` })
    },
  },
}
</script>

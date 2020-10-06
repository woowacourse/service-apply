<template>
  <CommonItem
    :title="recruitment.title"
    :start-date-time="startDateTime"
    :end-date-time="endDateTime"
    :buttonLabel="buttonLabel"
    :activeButton="this.isRecruiting"
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
  },
  computed: {
    startDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.startDateTime))
    },
    endDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.endDateTime))
    },
    isRecruiting() {
      return this.recruitment.status === "RECRUITING"
    },
    buttonLabel() {
      switch (this.recruitment.status) {
        case "RECRUITING":
          return "지원하기"
        case "RECRUITABLE":
          return "모집 예정"
        case "UNRECRUITABLE":
          return "일시 중지"
        case "ENDED":
          return "모집 종료"
      }
      return ""
    },
  },
  methods: {
    onClickAdmission(id) {
      this.$router.push({ path: `/register/applicant`, query: { recruitmentId: id } })
    },
  },
}
</script>

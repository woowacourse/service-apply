<template>
  <CommonItem
    :title="recruitment.title"
    :start-date-time="startDateTime"
    :end-date-time="endDateTime"
    :buttonLabel="buttonLabel"
    :activeButton="submittable"
    @click="goApplicationFormsEditPage"
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
      if (this.submitted) {
        return "제출 완료"
      }
      return this.submittable ? "지원서 수정" : "기간 만료"
    },
    submittable() {
      return !this.submitted && this.recruitment.status === "RECRUITING"
    },
    startDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.startDateTime))
    },
    endDateTime() {
      return parseLocalDateTime(new Date(this.recruitment.endDateTime))
    },
  },
  methods: {
    goApplicationFormsEditPage() {
      this.$router.push({
        path: `/application-forms/edit`,
        query: { recruitmentId: this.recruitment.id },
      })
    },
  },
}
</script>

<template>
  <CommonItem
    :recruitment="recruitment"
    :buttonLabel="buttonLabel"
    :activeButton="submittable"
    @click="goApplicationFormsEditPage"
  />
</template>

<script>
import CommonItem from "@/components/CommonItem"

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

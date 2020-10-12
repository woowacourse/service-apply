<template>
  <CommonItem
    :recruitment="recruitment"
    :buttonLabel="buttonLabel"
    :activeButton="isRecruiting"
    @click="goApplicantsNewPage"
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
  },
  computed: {
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
    goApplicantsNewPage() {
      this.$router.push({ path: `/applicants/new`, query: { recruitmentId: this.recruitment.id } })
    },
  },
}
</script>

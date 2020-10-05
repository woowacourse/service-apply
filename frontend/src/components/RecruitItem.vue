<template>
  <div class="list-wrapper card">
    <div class="flex space-between">
      <div>
        <div class="recruit-title">
          <b>{{ recruitment.title }}</b>
        </div>
        <div class="recruit-duration">{{ startTime }} ~ {{ endTime }}</div>
      </div>
      <div class="button-wrapper">
        <Button
          class="enroll-button button"
          @click="onClickAdmission(recruitment.id)"
          :disabled="!this.isRecruiting"
          :value="buttonLabel"
        >
        </Button>
      </div>
    </div>
  </div>
</template>

<script>
import Button from "@/components/form/Button"
import { parseLocalDateTime } from "@/utils/date"

export default {
  components: {
    Button,
  },
  props: {
    recruitment: {
      type: Object,
      required: true,
    },
  },
  computed: {
    startTime() {
      return parseLocalDateTime(this.recruitment.startTime)
    },
    endTime() {
      return parseLocalDateTime(this.recruitment.endTime)
    },
    isRecruiting() {
      return this.recruitment.recruitmentStatus === "RECRUITING"
    },
    buttonLabel() {
      switch (this.recruitment.recruitmentStatus) {
        case "RECRUITING": {
          return "지원하기"
        }
        case "RECRUITABLE": {
          return "모집 예정"
        }
        case "UNRECRUITABLE": {
          return "일시 중지"
        }
        case "ENDED": {
          return "모집 종료"
        }
        default: {
          throw "올바르지 않은 지원 타입입니다"
        }
      }
    },
  },
  methods: {
    onClickAdmission(id) {
      this.$router.push({ path: `/register/applicant`, query: { recruitmentId: id } })
    },
  },
}
</script>

<style>
@media (max-width: 440px) {
  .card {
    height: 90px !important;
  }

  .button-wrapper {
    line-height: 90px !important;
  }

  .list-wrapper {
    height: 90px !important;
  }
}

.card {
  width: 100%;
  height: 70px;
  background-color: #ffffff;
  border-radius: 3px;
  display: inline-block;
  margin: 5px 0 5px 0;
  box-shadow: 0 0 7px rgba(0, 0, 0, 0.05);
}

.recruit-title {
  padding: 10px 0px 5px 10px;
  font-size: large;
}

.recruit-duration {
  padding: 5px 0px 10px 10px;
}

.flex {
  display: flex;
  width: 100%;
  align-items: center;
  justify-content: center;
}

.list-wrapper {
  display: flex;
  height: 70px;
}

.space-between {
  justify-content: space-between;
}

.enroll-button {
  width: 120px;
  height: 40px;
}

.button-wrapper {
  margin-right: 12px;
}
</style>

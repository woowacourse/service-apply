<template>
  <div class="list-wrapper card">
    <div class="flex space-between">
      <div>
        <div class="recruit-title">
          <b>{{ recruitment.title }}</b>
        </div>
        <div class="recruit-duration">
          {{ parseTime(recruitment.startTime) }} ~ {{ parseTime(recruitment.endTime) }}
        </div>
      </div>
      <div class="button-wrapper">
        <Button
          class="enroll-button button"
          @click="onClickAdmission(recruitment.id)"
          :disabled="submitted"
          :value="buttonLabel"
        >
        </Button>
      </div>
    </div>
  </div>
</template>

<script>
import Button from "@/components/form/Button"
export default {
  components: {
    Button,
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
  },
  methods: {
    onClickAdmission(id) {
      this.$router.push({ path: `/register/applicant/${id}` })
    },
    parseTime(time) {
      const year = time.getFullYear().toString()
      const month = (time.getMonth() + 1).toString()
      const date = time.getDate().toString()
      const hour = time.getHours().toString()
      const minute = time.getMinutes().toString()
      const second = time.getSeconds().toString()
      const parseDigit = digit => {
        return digit[1] ? digit : "0" + digit[0]
      }

      return `${year}.${parseDigit(month)}.${parseDigit(date)}
      ${parseDigit(hour)}:${parseDigit(minute)}:${parseDigit(second)}`
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
  margin: 10px;
  vertical-align: middle;
}

.button-wrapper {
  margin-right: 12px;
}
</style>

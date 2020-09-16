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
        <button
          class="enroll-button button"
          @click="onClickAdmission(recruitment.id)"
          v-if="this.isRecruiting()"
        >
          {{ buttonLabel() }}
        </button>
        <button
            class="enroll-button button" disabled
            v-if="!this.isRecruiting()"
        >
          {{ buttonLabel() }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    recruitment: {
      type: Object,
      required: true,
    },
  },
  methods: {
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
    onClickAdmission(id) {
      this.$router.push({
        path: `/application/${id}`,
      })
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

.button {
  cursor: pointer;
  outline: none;
  border: 0;
  background: #0078ff;
  color: #fff;
  border-radius: 3px;
  padding: 10px;
  min-width: 80px;
  min-height: 28px;
  margin: 5px;
}

.button:disabled {
  cursor: default;
  background: #ccc;
}
</style>

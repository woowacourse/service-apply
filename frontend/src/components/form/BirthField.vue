<template>
  <Field class="birth-field">
    <Label for="year" :required="required">생년월일</Label>
    <div class="birth">
      <TextInput
        v-model="year"
        class="year"
        id="year"
        name="year"
        type="text"
        placeholder="YYYY"
        :required="required"
      />
      <TextInput
        v-model="month"
        class="month"
        name="month"
        type="text"
        placeholder="MM"
        :required="required"
      />
      <TextInput
        v-model="day"
        class="day"
        name="day"
        type="text"
        placeholder="DD"
        :required="required"
      />
    </div>
    <RuleField v-show="incorrectYear" v-model="incorrectYear" :rules="yearRules" :target="year" />
    <RuleField
      v-show="!incorrectYear && incorrectMonth"
      v-model="incorrectMonth"
      :rules="monthRules"
      :target="month"
    />
    <RuleField
      v-show="!incorrectYear && !incorrectMonth && incorrectDay"
      v-model="incorrectDay"
      :rules="dayRules"
      :target="day"
    />
  </Field>
</template>

<script>
import Label from "./Label"
import Field from "./Field"
import TextInput from "./TextInput"
import RuleField from "./RuleField"

const BirthField = {
  props: {
    value: {
      type: Object,
      default: () => ({
        year: "",
        month: "",
        day: "",
      }),
    },
    required: Boolean,
  },
  components: {
    Label,
    Field,
    TextInput,
    RuleField,
  },
  data: () => ({
    year: "",
    yearRules: [
      v => !!v || "필수 정보입니다.",
      v => v.length === 4 || "태어난 년도 4자리를 정확히 입력해 주세요.",
    ],
    month: "",
    monthRules: [
      v => !!v || "필수 정보입니다.",
      v => v.length === 2 || "태어난 월 2자리를 정확히 입력해 주세요.",
    ],
    day: "",
    dayRules: [
      v => !!v || "필수 정보입니다.",
      v => v.length === 2 || "태어난 일 2자리를 정확히 입력해 주세요.",
    ],
    incorrectYear: false,
    incorrectMonth: false,
    incorrectDay: false,
  }),
  created() {
    this.year = this.value.year
    this.month = this.value.month
    this.day = this.value.day
  },
  watch: {
    year() {
      this.updateInput()
    },
    month() {
      this.updateInput()
    },
    day() {
      this.updateInput()
    },
  },
  methods: {
    updateInput() {
      this.$emit("input", {
        year: this.year,
        month: this.month,
        day: this.day,
      })
    },
  },
}

export default BirthField
</script>

<style scoped>
.birth {
  display: flex;
  align-items: center;
  width: 100%;
}

.year,
.month,
.day {
  width: 100%;
}

.month {
  margin: 0 15px;
}
</style>

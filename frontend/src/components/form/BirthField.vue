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
    <RuleField v-show="incorrectYear" v-model="incorrectYear" :rules="rules.year" :target="year" />
    <RuleField
      v-show="incorrectMonth"
      v-model="incorrectMonth"
      :rules="rules.month"
      :target="month"
    />
    <RuleField v-show="incorrectDay" v-model="incorrectDay" :rules="rules.day" :target="day" />
  </Field>
</template>

<script>
import Label from "./Label"
import Field from "./Field"
import TextInput from "./TextInput"
import RuleField from "./RuleField"

import { birth } from "@/utils/validation"

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
    month: "",
    day: "",
    rules: { ...birth },
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
      this.$nextTick(() => {
        this.$emit(
          "valid",
          this.incorrectYear === true && this.incorrectMonth === true && this.incorrectDay === true,
        )
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

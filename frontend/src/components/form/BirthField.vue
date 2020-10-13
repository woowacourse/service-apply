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
  </Field>
</template>

<script>
import Label from "./Label"
import Field from "./Field"
import TextInput from "./TextInput"

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
  },
  data: () => ({
    year: "",
    month: "",
    day: "",
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

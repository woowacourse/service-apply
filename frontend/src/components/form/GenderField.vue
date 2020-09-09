<template>
  <Field class="gender-field">
    <Label target="male" required>성별</Label>
    <div class="gender-group">
      <Radio name="gender" id="male" label="남자" option="male" v-model="gender" required />
      <Radio name="gender" id="female" label="여자" option="female" v-model="gender" required />
    </div>
  </Field>
</template>

<script>
import Label from "./Label"
import Field from "./Field"
import Radio from "./Radio"

export const GenderField = {
  props: {
    value: String,
  },
  components: {
    Label,
    Field,
    Radio,
  },
  data: () => ({
    gender: "",
  }),
  created() {
    this.gender = this.value || ""
  },
  watch: {
    gender() {
      this.$emit("input", this.gender)
    },
  },
}
export default GenderField
</script>

<style scoped>
.gender-group {
  display: flex;
  margin-right: 10px;
  font-size: 0.8em;
}
</style>

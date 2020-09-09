<template>
  <Field class="gender-field">
    <Label required>성별</Label>
    <div class="gender-group">
      <Radio name="gender" label="남자" value="male" v-model="gender" required />
      <Radio name="gender" label="여자" value="female" v-model="gender" required />
    </div>
  </Field>
</template>

<script>
import Label from "./Label"
import Field from "./Field"
import Radio from "./Radio"

export const GenderField = {
  components: {
    Label,
    Field,
    Radio,
  },
  props: {
    value: String,
  },
  data: () => ({
    gender: "",
  }),
  watch: {
    gender() {
      this.$emit("input", this.gender)
    },
  },
  created() {
    this.gender = this.value || ""
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

<template>
  <Field class="text-field">
    <Label :for="name" :required="required">{{ label }}</Label>
    <TextInput
      :id="name"
      :name="name"
      :type="type"
      :placeholder="placeholder"
      :required="required"
      v-model="text"
    />
  </Field>
</template>

<script>
import Label from "./Label"
import Field from "./Field"
import TextInput from "./TextInput"

export const TextField = {
  components: {
    Label,
    Field,
    TextInput,
  },
  props: {
    value: String,
    label: String,
    type: String,
    placeholder: String,
    name: String,
    required: Boolean,
  },
  data: () => ({
    text: "",
  }),
  created() {
    this.text = this.value
  },
  watch: {
    text() {
      this.$emit("input", this.text)
    },
  },
}

export default TextField
</script>

<style scoped>
.text-field {
  display: flex;
  flex-direction: column;
}
</style>

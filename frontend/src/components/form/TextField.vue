<template>
  <Field class="text-field">
    <label class="text-field" @click="$event.currentTarget.click()">
      <div>
        <Label :required="required">{{ label }}</Label>
      </div>
      <TextInput v-bind="$attrs" :required="required" v-model="text" />
    </label>
    <RuleField :rules="rules" :target="text" />
  </Field>
</template>

<script>
import Label from "./Label"
import Field from "./Field"
import TextInput from "./TextInput"
import RuleField from "./RuleField"

const TextField = {
  components: {
    Label,
    Field,
    TextInput,
    RuleField,
  },
  inheritAttrs: false,
  props: {
    label: String,
    rules: {
      type: Array,
      default: [],
    },
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

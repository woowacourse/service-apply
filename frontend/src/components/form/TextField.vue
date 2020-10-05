<template>
  <Field class="text-field">
    <label class="text-field" @click="$event.currentTarget.click()">
      <div>
        <Label :required="required">{{ label }}</Label>
      </div>
      <Description v-if="description">{{ description }}</Description>
      <div v-if="maxLength > 0" class="length-limit">{{ text.length }} / {{ maxLength }}</div>
      <TextInput v-bind="$attrs" :required="required" v-model="text" :max-length="maxLength" />
    </label>
    <RuleField @input="valid" :rules="rules" :target="text" />
  </Field>
</template>

<script>
import Label from "./Label"
import Field from "./Field"
import TextInput from "./TextInput"
import RuleField from "./RuleField"
import Description from "./Description"

const TextField = {
  components: {
    Label,
    Field,
    TextInput,
    RuleField,
    Description,
  },
  inheritAttrs: false,
  props: {
    label: String,
    rules: {
      type: Array,
      default: () => [],
    },
    value: String,
    required: Boolean,
    description: String,
    maxLength: Number,
  },
  created() {
    this.text = this.value
  },
  data: () => ({
    text: "",
  }),
  watch: {
    text() {
      this.$emit("input", this.text)
    },
    value() {
      this.text = this.value
    },
  },
  methods: {
    valid(v) {
      this.$emit("valid", v === true)
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

.length-limit {
  align-self: flex-end;
  color: #999999;
  font-size: 13px;
}
</style>

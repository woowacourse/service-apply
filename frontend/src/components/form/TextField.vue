<template>
  <Field class="text-field">
    <label class="text-field" @click="$event.currentTarget.click()">
      <div>
        <Label :required="required">{{ label }}</Label>
      </div>
      <div v-if="description" class="description">
        {{ description }}
      </div>
      <div v-if="maxLength > 0" class="length-limit">
        {{ text.length }} / {{ maxLength }}
      </div>
      <TextInput v-bind="$attrs" :required="required" v-model="text" :max-length="maxLength" />
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
    description: String,
    maxLength: Number,
  },
  data: () => ({
    text: "",
  }),
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

.description {
  display: flex;
  margin: 15px 0;
  font-weight: 300;
}

.length-limit {
  align-self: flex-end;
  color: #999999;
  font-size: 13px;
}
</style>

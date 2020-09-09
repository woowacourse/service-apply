<template>
  <Field>
    <label>
      <CheckBox :name="name" :label="label" v-model="checked" required />
    </label>
    <div class="summary" tabindex="-1">
      <slot class="text"></slot>
    </div>
  </Field>
</template>

<script>
import Field from "./Field"
import CheckBox from "./CheckBox"

export const SummaryCheckField = {
  components: {
    Field,
    CheckBox,
  },
  props: {
    name: String,
    label: String,
    value: Boolean,
  },
  data: () => ({
    checked: false,
  }),
  created() {
    this.checked = this.value
  },
  watch: {
    checked() {
      this.$emit("input", this.checked)
    },
  },
}

export default SummaryCheckField
</script>

<style scoped>
.summary {
  overflow-y: auto;
  background: #fff;
  outline: none;
  margin: 0;
  padding: 12px;
  max-height: 140px;
  border: 1px solid #ced6e0;
  border-radius: 3px;
  transition-duration: 0.5s;
}

.summary:focus {
  border: 1px solid #1e90ff;
}
</style>

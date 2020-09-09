<template>
  <Field>
    <CheckBox v-bind="$attrs" abc="ad" :label="label" v-model="selected" required />
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
  inheritAttrs: false,
  model: {
    prop: "checked",
    event: "change",
  },
  props: {
    checked: Boolean,
    label: String,
  },
  data: () => ({
    selected: false,
  }),
  watch: {
    selected() {
      this.$emit("change", this.selected)
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

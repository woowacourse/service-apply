<template>
  <Label class="checkbox" :class="{ checked }" :required="required">
    {{ label }}
    <input
      type="checkbox"
      v-bind="$attrs"
      :required="required"
      @input="$emit('change', $event.target.checked)"
    />
  </Label>
</template>

<script>
import Label from "./Label"

const CheckBox = {
  components: {
    Label,
  },
  inheritAttrs: false,
  model: {
    prop: "checked",
    event: "change",
  },
  props: {
    label: String,
    checked: Boolean,
    required: Boolean,
  },
}

export default CheckBox
</script>

<style scoped>
input[type="checkbox"] {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  display: none;
  outline: none;
}

.checkbox {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.checkbox::before {
  content: "✓";
  display: flex;
  justify-content: center;
  align-items: center;
  width: 24px;
  height: 24px;
  border-radius: 24px;
  border: 1px solid #ced6e0;
  margin-right: 5px;
  background: #fff;
  transition-duration: 0.2s;
}

.checked::before {
  background: #0078ff;
  border: 1px solid #e2eaf4;
  color: #fff;
}
</style>

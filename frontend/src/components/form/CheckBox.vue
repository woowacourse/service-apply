<template>
  <div class="checkbox">
    <input
      type="checkbox"
      :id="name"
      :name="name"
      :checked="checked"
      @input="$emit('input', (checked = !checked))"
      :required="required"
    />
    <Label :for="name" :required="required">{{ label }}</Label>
  </div>
</template>

<script>
import Label from "./Label"

export const CheckBox = {
  components: {
    Label,
  },
  props: {
    value: Boolean,
    name: String,
    label: String,
    required: Boolean,
  },
  created() {
    this.checked = this.value
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

label {
  display: flex;
  align-items: center;
  cursor: pointer;
}

label::before {
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

input[type="checkbox"]:checked + label::before {
  background: #0078ff;
  border: 1px solid #e2eaf4;
  color: #fff;
}
</style>

<template>
  <div class="radio">
    <input
      type="radio"
      :id="name + '-' + option"
      :name="name"
      :value="option"
      :checked="option === value"
      @input="$emit('input', option)"
      :required="required"
    />
    <Label :for="name + '-' + option">{{ label }}</Label>
  </div>
</template>

<script>
import Label from "./Label"

export const Radio = {
  components: {
    Label,
  },
  props: {
    name: String,
    label: String,
    option: null,
    value: null,
    required: Boolean,
  },
}

export default Radio
</script>

<style scoped>
.radio {
  display: flex;
  align-items: center;
  margin-right: 10px;
}

input[type="radio"] {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  position: absolute;
  margin-left: 13px;
}

label {
  display: flex;
  align-items: center;
  cursor: pointer;
}

label::before {
  content: "";
  display: flex;
  justify-content: center;
  align-items: center;
  width: 20px;
  height: 20px;
  border-radius: 20px;
  border: 3px solid #ced6e0;
  background: #fff;
  margin-right: 5px;
  transition-duration: 0.2s;
}

input[type="radio"]:checked + label::before {
  background: #0078ff;
  border: 3px solid #d8e0ea;
  color: #fff;
}
</style>

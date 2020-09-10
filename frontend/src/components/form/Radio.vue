<template>
  <Label class="radio" :class="{ checked: checked === $attrs.value }">
    {{ label }}
    <input
      type="radio"
      v-bind="$attrs"
      @change="$emit('change', $event.target.value)"
      :required="required"
    />
  </Label>
</template>

<script>
import Label from "./Label"

const Radio = {
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
    checked: String,
    required: Boolean,
  },
}

export default Radio
</script>

<style scoped>
input[type="radio"] {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  z-index: -9999;
  width: 1px;
  height: 1px;
  position: absolute;
  margin-left: 13px;
}

.radio {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-right: 10px;
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

.checked::before {
  background: #0078ff;
  border: 3px solid #d8e0ea;
  color: #fff;
}
</style>

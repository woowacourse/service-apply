<template>
  <input
    :type="type"
    class="text-input"
    :value="value"
    @input="$emit('input', $event.target.value)"
  />
</template>

<script>
const TextInput = {
  props: {
    type: {
      type: String,
      default: "text",
      validator(value) {
        return ["text", "email", "password"].indexOf(value) !== -1
      },
    },
    value: String,
  },
}

export default TextInput
</script>

<style scoped>
input[type="text"],
input[type="email"],
input[type="password"] {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  outline: none;
}

.text-input {
  padding: 12px;
  border: 1px solid #ced6e0;
  border-radius: 3px;
  transition-duration: 0.5s;
}

.text-input:focus {
  border: 1px solid #1e90ff;
}
</style>

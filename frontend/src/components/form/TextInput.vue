<template>
  <input class="text-input" :value="value" @input="updateInput" />
</template>

<script>
export const TextInput = {
  props: {
    value: String,
  },
  methods: {
    updateInput({ target }) {
      this.$emit("input", target.value)
    },
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

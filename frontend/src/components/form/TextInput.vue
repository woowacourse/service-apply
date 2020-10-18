<template>
  <textarea
    v-if="type === 'textarea'"
    class="text-input"
    :value="value"
    :maxlength="maxLength"
    @input="$emit('input', $event.target.value)"
  />
  <input
    v-else
    :type="type"
    class="text-input"
    :value="value"
    @input="$emit('input', $event.target.value)"
    :readonly="readonly"
  />
</template>

<script>
const TextInput = {
  props: {
    type: {
      type: String,
      default: "text",
      validator(value) {
        return ["text", "email", "password", "textarea", "url"].indexOf(value) !== -1
      },
    },
    readonly: Boolean,
    value: String,
    maxLength: {
      default: 0,
      type: Number,
    },
  },
}

export default TextInput
</script>

<style scoped>
input[type="text"],
input[type="email"],
input[type="url"],
input[type="password"],
textarea {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  outline: none;
}

@media screen and (min-width: 513px) {
  textarea {
    min-height: 200px !important;
  }
}

.text-input {
  padding: 12px;
  border: 1px solid #ced6e0;
  border-radius: 3px;
  transition-duration: 0.5s;
}

textarea.text-input {
  min-width: 100%;
  max-width: 100%;
  min-height: 100px;
}

.text-input:focus {
  border: 1px solid #1e90ff;
}

.text-input:read-only {
  cursor: default;
  background: #ccc;
}
</style>

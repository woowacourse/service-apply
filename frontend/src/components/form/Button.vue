<template>
  <input :type="type" class="button" :class="{ cancel }" v-on="$listeners" />
</template>

<script>
const Button = {
  props: {
    type: {
      type: String,
      default: "button",
      validator(value) {
        return ["button", "submit", "reset", "save"].indexOf(value) !== -1
      },
    },
    cancel: Boolean,
  },
}

export default Button
</script>

<style scoped>
.button {
  cursor: pointer;
  outline: none;
  border: 0;
  background: #0078ff;
  color: #fff;
  border-radius: 3px;
  padding: 10px;
  min-width: 80px;
  min-height: 28px;
  margin: 5px;
}

.button.cancel {
  background: #999;
}

.button:disabled {
  cursor: default;
  background: #ccc;
}
</style>

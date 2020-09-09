<template>
  <input class="button" :class="{ cancel }" />
</template>

<script>
export const Button = {
  props: {
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

<template>
  <div class="field">
    <slot></slot>
  </div>
</template>

<script>
export const Field = {}
export default Field
</script>

<style scoped>
.field {
  display: flex;
  flex-direction: column;
  margin: 10px 0;
}
</style>

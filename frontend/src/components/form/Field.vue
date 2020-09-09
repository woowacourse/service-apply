<template>
  <div class="field">
    <slot></slot>
  </div>
</template>

<script>
import Label from "./Label"

export const Field = {
  components: {
    Label,
  },
  props: ["label", "required"],
}
export default Field
</script>

<style scoped>
.field {
  display: flex;
  flex-direction: column;
  margin: 10px 0;
}
</style>

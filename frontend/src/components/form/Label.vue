<template>
  <label class="label" :class="{ required }" v-on="$listeners"><slot></slot></label>
</template>

<script>
const Label = {
  props: {
    required: Boolean,
  },
}

export default Label
</script>

<style scoped>
.label {
  display: flex;
  align-items: center;
  font-weight: 500;
  color: #2c3e50;
  margin: 5px 0;
}

.required::after {
  content: "*";
  margin: 0 3px;
  color: #1e90ff;
}
</style>

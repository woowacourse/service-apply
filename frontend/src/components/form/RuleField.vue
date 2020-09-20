<template>
  <div class="rule-field">
    <span v-if="incorrect && incorrect !== true">{{ incorrect }}</span>
  </div>
</template>

<script>
const RuleField = {
  props: {
    rules: Array,
    target: String,
    value: null,
  },
  data: () => ({
    incorrect: false,
  }),
  watch: {
    target() {
      const result = this.rules.reduce((prev, curr) => prev && curr(this.target), true)
      this.incorrect = result === true ? true : result
      this.$emit("input", this.incorrect)
    },
  },
}

export default RuleField
</script>

<style scoped>
.rule-field > span {
  font-size: 12px;
  color: #ff0000;
}
</style>

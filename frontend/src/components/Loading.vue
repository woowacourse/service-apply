<template>
  <Loading :active.sync="show" background-color="#000" blur="0.5px"></Loading>
</template>

<script>
import Loading from "vue-loading-overlay"
import "vue-loading-overlay/dist/vue-loading.css"

export default {
  props: {
    show: {
      type: Boolean,
      required: true,
    },
  },
  components: {
    Loading,
  },
}
</script>

<style scoped></style>

<template>
  <form class="form" v-on="$listeners">
    <slot></slot>
  </form>
</template>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 512px;
  padding: 20px;
  margin: 15px;
  border-radius: 3px;
  background: #f1f2f6;
  box-shadow: 0 0 7px rgba(0, 0, 0, 0.05);
}

@media screen and (max-width: 512px) {
  .form {
    margin: 0;
  }
}

.form > h1 {
  color: #2c3e50;
}
</style>

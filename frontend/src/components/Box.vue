<template>
  <div class="box">
    <slot></slot>
  </div>
</template>

<style scoped>
.box {
  display: flex;
  flex-direction: column;
  width: 800px;
  padding: 20px;
  margin: 15px;
  border-radius: 3px;
  background: #f1f2f6;
  box-shadow: 0 0 7px rgba(0, 0, 0, 0.05);
}

@media screen and (max-width: 800px) {
  .box {
    width: 100vw;
    margin: 0;
  }
}

.box > h1 {
  color: #2c3e50;
}
</style>

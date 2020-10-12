<template>
  <form class="form" v-on="$listeners">
    <slot></slot>
    <div class="actions">
      <slot name="actions"></slot>
    </div>
    <footer>
      <a class="logo" href="#"></a>
      <slot name="footer"></slot>
    </footer>
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

.actions {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}

.actions > .button {
  flex: 1;
}

.logo {
  display: flex;
  width: 100px;
  height: 32px;
  background: url("/assets/logo/logo_full_dark.png");
  background-size: 100% 100%;
}

footer {
  display: flex;
  justify-content: space-between;
}
</style>

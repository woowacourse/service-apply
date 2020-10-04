<template>
  <div class="main-header">
    <header class="header">
      <nav class="nav">
        <a target="_blank" href="https://woowacourse.github.io/">
          <img class="logo" src="/assets/logo/logo_full_dark.png" />
        </a>
        <h3 class="title">
          <router-link to="/recruits">지원하기</router-link>
        </h3>
      </nav>
    </header>
  </div>
</template>

<script>
export default {}
</script>

<style scoped>
.main-header {
  margin-bottom: 93px;
}

.header {
  position: fixed;
  top: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 93px;
  background: #fff;
  box-shadow: 0px 4px 6px 0px rgba(12, 0, 46, 0.06);
  z-index: 9999;
}

.nav {
  display: flex;
  align-items: center;
  width: 830px;
  padding: 0 15px;
}

.logo {
  width: 200px;
}

.title {
  font-size: 22px;
}

a {
  text-decoration: none;
  color: #444;
}
</style>

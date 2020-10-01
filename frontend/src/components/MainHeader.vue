<template>
  <div class="main-header">
    <header class="header">
      <div class="container">
        <a target="_blank" href="https://woowacourse.github.io/"
          ><img class="logo" src="/assets/logo/logo_full_dark.png"
        /></a>
        <h3 class="title"><router-link to="/recruits">지원</router-link></h3>
      </div>
    </header>
  </div>
</template>

<script>
export default {}
</script>

<style scoped>
.main-header {
  margin-bottom: 80px;
}

.header {
  position: fixed;
  top: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 80px;
  background: #fff;
  border-bottom: 1px solid #f1f1f1;
}

.container {
  display: flex;
  align-items: center;
  width: 800px;
}

.logo {
  width: 180px;
  height: 60px;
}

.title {
  font-size: 22px;
}

a {
  text-decoration: none;
  color: #444;
}
</style>

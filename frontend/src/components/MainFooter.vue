<template>
  <footer class="main-footer">
    <div class="container">
      <div class="logo-title">
        <a target="_blank" href="https://woowacourse.github.io/"
          ><img class="logo" src="/assets/logo/logo_full_white.png"
        /></a>
        <h3 class="title"><router-link to="/recruits">지원</router-link></h3>
      </div>
      <div class="copyright">Copyright © 우아한테크코스. All rights reserved</div>
    </div>
  </footer>
</template>

<script>
export default {}
</script>

<style scoped>
.main-footer {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 30px 0;
  background: #333;
  color: #fff;
}

.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 800px;
}

.logo-title {
  display: flex;
  align-items: center;
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
  color: #fff;
}

.copyright {
  margin: 10px;
}
</style>

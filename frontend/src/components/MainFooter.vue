<template>
  <footer class="main-footer">
    <div class="container">
      <div class="logo-title">
        <a target="_blank" href="https://woowacourse.github.io/">
          <img class="logo" src="/assets/logo/logo_full_white.png" />
        </a>
      </div>
      <div class="copyright">Copyright © 우아한테크코스. All rights reserved</div>
      <div class="social">
        <!-- 3기 모집 안내문 링크로 수정 필요 -->
        <a
          href="https://woowabros.github.io/techcourse/2019/10/14/woowacourse.html"
          target="_blank"
          class="ti-github"
        ></a>
        <a
          href="https://www.youtube.com/channel/UC-mOekGSesms0agFntnQang/featured?view_as=subscribe"
          target="_blank"
          class="fa fa-youtube-play"
        ></a>
      </div>
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
  flex: 1 0 auto;
}

.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 830px;
  padding: 0 15px;
}

.logo-title {
  display: flex;
  align-items: center;
}

.logo {
  width: 200px;
}

.title {
  font-size: 22px;
}

a {
  text-decoration: none;
  color: #fff;
}

.social {
  display: flex;
  margin: 20px 0 0 10px;
}

.social a {
  display: flex;
  width: 46px;
  height: 46px;
  justify-content: center;
  align-items: center;
  border-radius: 100%;
  border: 1px solid #fff;
  transition: all 0.2s linear;
  margin-right: 10px;
}

.social a:hover {
  color: #333;
  background-color: #fff;
}

.copyright {
  margin: 10px;
}
</style>

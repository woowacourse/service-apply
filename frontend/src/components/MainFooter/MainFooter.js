import React from "react";

import "./MainFooter.css";

const MainFooter = () => {
  return (
    <footer class="main-footer">
      <div class="container">
        <div class="logo-title">
          <a
            target="_blank"
            href="https://woowacourse.github.io/"
            rel="noreferrer"
          >
            <img class="logo" src="/assets/logo/logo_full_white.png" />
          </a>
        </div>
        <div class="copyright">
          Copyright © 우아한테크코스. All rights reserved
        </div>
        <div class="social">
          <a
            href="https://woowabros.github.io/techcourse/2020/10/06/woowacourse.html"
            target="_blank"
            class="ti-github"
            rel="noreferrer"
          ></a>
          <a
            href="https://www.youtube.com/channel/UC-mOekGSesms0agFntnQang/featured?view_as=subscribe"
            target="_blank"
            class="fa fa-youtube-play"
            rel="noreferrer"
          ></a>
        </div>
      </div>
    </footer>
  );
};

export default MainFooter;

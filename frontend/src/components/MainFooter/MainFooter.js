import React from "react";

import "./MainFooter.css";

const MainFooter = () => {
  return (
    <footer className="main-footer">
      <div className="container">
        <div className="logo-title">
          <a
            target="_blank"
            href="https://woowacourse.github.io/"
            rel="noreferrer"
          >
            <img className="logo" src="/assets/logo/logo_full_white.png" />
          </a>
        </div>
        <div className="copyright">
          Copyright © 우아한테크코스. All rights reserved
        </div>
        <div className="social">
          <a
            href="https://woowabros.github.io/techcourse/2020/10/06/woowacourse.html"
            target="_blank"
            className="ti-github"
            rel="noreferrer"
          ></a>
          <a
            href="https://www.youtube.com/channel/UC-mOekGSesms0agFntnQang/featured?view_as=subscribe"
            target="_blank"
            className="fa fa-youtube-play"
            rel="noreferrer"
          ></a>
        </div>
      </div>
    </footer>
  );
};

export default MainFooter;

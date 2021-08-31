import React from "react";

import styles from "./MainFooter.module.css";

const MainFooter = () => {
  return (
    <footer className={styles["main-footer"]}>
      <div className={styles.container}>
        <div className={styles["logo-title"]}>
          <a
            target="_blank"
            href="https://woowacourse.github.io/"
            rel="noreferrer"
          >
            <img
              className={styles.logo}
              src="/assets/logo/logo_full_white.png"
              alt="우아한테크코스 로고"
            />
          </a>
        </div>
        <div className={styles.copyright}>
          Copyright © 우아한테크코스. All rights reserved
        </div>
        <div className={styles.social}>
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

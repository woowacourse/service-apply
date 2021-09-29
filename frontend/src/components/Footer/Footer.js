import React from "react";

import styles from "./Footer.module.css";

const Footer = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles["content-wrapper"]}>
        <a
          target="_blank"
          href="https://woowacourse.github.io/"
          rel="noreferrer noopener"
        >
          <img
            className={styles.logo}
            src="/assets/logo/logo_full_white.png"
            alt="우아한테크코스 로고"
          />
        </a>
        <p className={styles.copyright}>
          Copyright 2021. 우아한테크코스. All rights reserved.
        </p>
        <div className={styles.social}>
          <a
            href="https://woowabros.github.io/techcourse/2020/10/06/woowacourse.html"
            target="_blank"
            className="ti-github"
            rel="noreferrer noopener"
            aria-label="GitHub"
          ></a>
          <a
            href="https://www.youtube.com/channel/UC-mOekGSesms0agFntnQang/featured?view_as=subscribe"
            target="_blank"
            className="fa fa-youtube-play"
            rel="noreferrer noopener"
            aria-label="Youtube"
          ></a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;

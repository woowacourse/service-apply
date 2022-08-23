import React from "react";
import styles from "./Footer.module.css";

const URL = {
  LOGO: "https://woowacourse.github.io/",
  GITHUB: "https://github.com/woowacourse/",
  YOUTUBE: "https://www.youtube.com/channel/UC-mOekGSesms0agFntnQang/featured?view_as=subscribe",
  INSTAGRAM: "https://instagram.com/wooteco?igshid=YmMyMTA2M2Y",
};

const Footer = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles["content-wrapper"]}>
        <a target="_blank" href={URL.LOGO} rel="noreferrer noopener">
          <img
            className={styles.logo}
            src="/assets/logo/logo_full_white.png"
            alt="우아한테크코스 로고"
          />
        </a>
        <div className={styles.social}>
          <a href={URL.GITHUB} target="_blank" rel="noreferrer noopener" aria-label="GitHub">
            <i className="ti-github" aria-hidden="true" />
          </a>
          <hr className={styles.divider} />
          <a href={URL.YOUTUBE} target="_blank" rel="noreferrer noopener" aria-label="Youtube">
            <i className="fa fa-youtube-play" aria-hidden="true" />
          </a>
          <hr className={styles.divider} />
          <a href={URL.INSTAGRAM} target="_blank" rel="noreferrer noopener" aria-label="Instagram">
            <i class="fa fa-instagram" aria-hidden="true" />
          </a>
        </div>
        <p className={styles.copyright}>Copyright 2021. 우아한테크코스. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;

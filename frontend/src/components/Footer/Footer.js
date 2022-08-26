import React from "react";
import styles from "./Footer.module.css";

import { FOOTER_LINK_URL } from "../../constants/url";

const Footer = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles["content-wrapper"]}>
        <a target="_blank" href={FOOTER_LINK_URL.LOGO} rel="noreferrer noopener">
          <img
            className={styles.logo}
            src="/assets/logo/logo_full_white.png"
            alt="우아한테크코스 로고"
          />
        </a>
        <div className={styles.social}>
          <a
            href={FOOTER_LINK_URL.GITHUB}
            target="_blank"
            rel="noreferrer noopener"
            aria-label="GitHub"
          >
            <i className="ti-github" aria-hidden="true" />
          </a>
          <hr className={styles.divider} />
          <a
            href={FOOTER_LINK_URL.YOUTUBE}
            target="_blank"
            rel="noreferrer noopener"
            aria-label="Youtube"
          >
            <i className="fa fa-youtube-play" aria-hidden="true" />
          </a>
          <hr className={styles.divider} />
          <a
            href={FOOTER_LINK_URL.INSTAGRAM}
            target="_blank"
            rel="noreferrer noopener"
            aria-label="Instagram"
          >
            <i className="fa fa-instagram" aria-hidden="true" />
          </a>
        </div>
        <p className={styles.copyright}>Copyright 2021. 우아한테크코스. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;

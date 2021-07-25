import React from "react";
import { Link } from "react-router-dom";

import styles from "./MainHeader.module.css";

const MainHeader = () => {
  return (
    <div className={styles["main-header"]}>
      <header className={styles.header}>
        <nav className={styles.nav}>
          <Link to="/recruits">
            <img
              className={styles.logo}
              src="/assets/logo/logo_full_dark.png"
              alt="우아한테크코스 로고"
            />
          </Link>
          <h3 className={styles.title}>
            <Link to="/recruits">지원하기</Link>
          </h3>
        </nav>
      </header>
    </div>
  );
};

export default MainHeader;

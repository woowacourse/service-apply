import React from "react";
import { Link } from "react-router-dom";
import PATH from "../../constants/path";
import styles from "./MainHeader.module.css";

const MainHeader = () => {
  return (
    <div className={styles["main-header"]}>
      <header className={styles.header}>
        <nav className={styles.nav}>
          <h1>
            <Link className={styles.title} to={PATH.RECRUITS}>
              <img
                className={styles.logo}
                src="/assets/logo/logo_full_dark.png"
                alt="우아한테크코스 로고"
              />
              지원하기
            </Link>
          </h1>
        </nav>
      </header>
    </div>
  );
};

export default MainHeader;

import React from "react";
import { Link } from "react-router-dom";

import useTokenContext from "../../hooks/useTokenContext";
import PATH from "../../constants/path";

import MemberIcon from "../../assets/icon/member-icon.svg";

import styles from "./Header.module.css";

const Header = () => {
  const { token } = useTokenContext();

  return (
    <div className={styles.container}>
      <header className={styles.header}>
        <div className={styles.content}>
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

          <div className={styles["link-container"]}>
            {token ? (
              <button type="button">
                <img src={MemberIcon} alt="회원" />
              </button>
            ) : (
              <>
                <Link to={PATH.LOGIN}>로그인</Link>
                <div className={styles.bar} />
                <Link to={PATH.NEW_APPLICATION}>회원가입</Link>
              </>
            )}
          </div>
        </div>
      </header>
    </div>
  );
};

export default Header;

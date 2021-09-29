import React, { useState } from "react";
import { Link } from "react-router-dom";

import useTokenContext from "../../hooks/useTokenContext";
import PATH from "../../constants/path";

import MemberIcon from "../../assets/icon/member-icon.svg";

import styles from "./Header.module.css";

const Header = () => {
  const { token, resetToken } = useTokenContext();

  const [isShowMemberMenu, setIsShowMemberMenu] = useState(false);

  const onChange = ({ target }) => {
    setIsShowMemberMenu(target.checked);
  };

  const onLogout = () => {
    setIsShowMemberMenu(false);
    resetToken();
  };

  return (
    <div className={styles.box}>
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
              <div className={styles["member-menu-container"]}>
                <label
                  className={styles["checkbox-label"]}
                  aria-label="회원관리 툴팁"
                >
                  <input
                    type="checkbox"
                    className={styles.checkbox}
                    onChange={onChange}
                  />
                  <img src={MemberIcon} alt="회원" />
                </label>

                {isShowMemberMenu && (
                  <>
                    <ul className={styles["member-menu-list"]}>
                      <li>
                        <Link>마이페이지</Link>
                      </li>
                      <li>
                        <Link>내 지원 현황</Link>
                      </li>
                      <li onClick={onLogout}>로그아웃</li>
                    </ul>
                    <div
                      className={styles.dimmed}
                      onMouseDown={() => setIsShowMemberMenu(false)}
                    />
                  </>
                )}
              </div>
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

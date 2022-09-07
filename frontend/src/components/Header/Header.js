import classNames from "classnames";
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import MemberIcon from "../../assets/icon/member-icon.svg";
import PATH from "../../constants/path";
import useTokenContext from "../../hooks/useTokenContext";
import styles from "./Header.module.css";

const Header = () => {
  const navigate = useNavigate();

  const { token, resetToken } = useTokenContext();

  const [isShowMemberMenu, setIsShowMemberMenu] = useState(false);

  const onChange = ({ target }) => {
    setIsShowMemberMenu(target.checked);
  };

  const routeTo = ({ pathname }) => {
    navigate(pathname);
    setIsShowMemberMenu(false);
  };

  const onLogout = () => {
    setIsShowMemberMenu(false);
    resetToken();
    navigate(PATH.HOME);
  };

  return (
    <div className={styles.box}>
      <header className={styles.header}>
        <div className={styles.content}>
          <h1>
            <Link className={styles.title} to={PATH.RECRUITS}>
              <img
                className={styles.logo}
                src="/assets/logo/logo_icon_dark.png"
                alt="우아한테크코스 로고"
              />
              <span>지원하기</span>
            </Link>
          </h1>

          <div className={styles["link-container"]}>
            {token ? (
              <div className={styles["member-menu-container"]}>
                <label
                  className={classNames(styles["checkbox-label"], {
                    [styles.checked]: isShowMemberMenu,
                    [styles.unchecked]: !isShowMemberMenu,
                  })}
                  aria-label="회원관리 툴팁"
                >
                  <img src={MemberIcon} alt="회원" />
                  <input
                    type="checkbox"
                    className={styles.checkbox}
                    checked={isShowMemberMenu}
                    onChange={onChange}
                  />
                </label>

                {isShowMemberMenu && (
                  <>
                    <ul className={styles["member-menu-list"]}>
                      <li className={styles["member-menu-listitem"]}>
                        <button type="button" onClick={() => routeTo({ pathname: PATH.MY_PAGE })}>
                          마이페이지
                        </button>
                      </li>
                      <li className={styles["member-menu-listitem"]}>
                        <button
                          type="button"
                          onClick={() => routeTo({ pathname: PATH.MY_APPLICATION })}
                        >
                          내 지원 현황
                        </button>
                      </li>
                      <li className={styles["member-menu-listitem"]} onClick={onLogout}>
                        로그아웃
                      </li>
                    </ul>
                    <div className={styles.dimmed} onMouseDown={() => setIsShowMemberMenu(false)} />
                  </>
                )}
              </div>
            ) : (
              <>
                <Link to={PATH.LOGIN}>로그인</Link>
                <div className={styles.bar} />
                <Link to={PATH.SIGN_UP}>회원가입</Link>
              </>
            )}
          </div>
        </div>
      </header>
    </div>
  );
};

export default Header;

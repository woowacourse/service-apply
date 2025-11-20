import classNames from "classnames";
import useLanguageSwitcher from "../../hooks/useLanguageSwitcher";
import styles from "./LanguageSwitcher.module.css";
import { RiGlobalLine } from "react-icons/ri";
import { IoTriangle } from "react-icons/io5";
import { useState } from "react";
import { SupportedLanguage } from "../../../types/domains/language";

const LanguageSwitcher = () => {
  const { selectLanguage } = useLanguageSwitcher();
  const [isShowLanguageSwitcher, setIsShowLanguageSwitcher] = useState(false);

  const handleClickLanguageItem = (language: SupportedLanguage) => {
    selectLanguage(language);
    setIsShowLanguageSwitcher(false);
  };

  return (
    <div className={styles["language-switcher-container"]}>
      <label
        className={classNames(styles["checkbox-label"], {
          [styles.checked]: isShowLanguageSwitcher,
          [styles.unchecked]: !isShowLanguageSwitcher,
        })}
        aria-label="언어 선택"
      >
        <div className={styles["language-icon-container"]}>
          <RiGlobalLine className={styles["language-icon"]} size={16} />
          <IoTriangle size={9} />
        </div>
        <input
          type="checkbox"
          className={styles.checkbox}
          checked={isShowLanguageSwitcher}
          onChange={() => setIsShowLanguageSwitcher(!isShowLanguageSwitcher)}
        />
      </label>
      {isShowLanguageSwitcher && (
        <>
          <ul className={`${styles["language-list"]} notranslate`}>
            <li className={styles["language-listitem"]}>
              <button
                className={styles["language-button"]}
                type="button"
                onClick={() => handleClickLanguageItem("ko")}
              >
                <span className={styles["language-flag"]}>🇰🇷</span>
                <span className={styles["language-code"]}>KR</span>
              </button>
            </li>
            <li className={styles["language-listitem"]}>
              <button
                className={styles["language-button"]}
                type="button"
                onClick={() => handleClickLanguageItem("en")}
              >
                <span className={styles["language-flag"]}>🇺🇸</span>
                <span className={styles["language-code"]}>EN</span>
              </button>
            </li>
            <li className={styles["language-listitem"]}>
              <button
                className={styles["language-button"]}
                type="button"
                onClick={() => handleClickLanguageItem("de")}
              >
                <span className={styles["language-flag"]}>🇩🇪</span>
                <span className={styles["language-code"]}>DE</span>
              </button>
            </li>
          </ul>
          <div className={styles.dimmed} onMouseDown={() => setIsShowLanguageSwitcher(false)} />
        </>
      )}
    </div>
  );
};

export default LanguageSwitcher;

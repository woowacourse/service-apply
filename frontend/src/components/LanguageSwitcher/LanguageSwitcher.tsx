import useLanguageSwitcher from "../../hooks/useLanguageSwitcher";
import styles from "./LanguageSwitcher.module.css";
import { RiGlobalLine } from "react-icons/ri";
import { useState } from "react";
import { SupportedLanguage } from "../../../types/domains/language";
import ToggleButton from "../@common/ToggleButton/ToggleButton";

const LanguageSwitcher = () => {
  const { selectLanguage } = useLanguageSwitcher();
  const [isShowLanguageSwitcher, setIsShowLanguageSwitcher] = useState(false);

  const handleClickLanguageItem = (language: SupportedLanguage) => {
    selectLanguage(language);
    setIsShowLanguageSwitcher(false);
  };

  return (
    <div className={styles["language-switcher-container"]}>
      <div className={styles["google-translate-hidden"]} id="google_translate_element" />
      <ToggleButton
        checked={isShowLanguageSwitcher}
        onChange={(checked) => setIsShowLanguageSwitcher(checked)}
        ariaLabel="언어 선택"
      >
        <div className={styles["language-icon-container"]}>
          <RiGlobalLine className={styles["language-icon"]} size={16} />
        </div>
      </ToggleButton>
      {isShowLanguageSwitcher && (
        <>
          <ul className={`${styles["language-list"]} notranslate`}>
            <li
              className={styles["language-listitem"]}
              onClick={() => handleClickLanguageItem("ko")}
            >
              <span className={styles["language-flag"]}>🇰🇷</span>
              <span className={styles["language-name"]}>한국어</span>
            </li>
            <li
              className={styles["language-listitem"]}
              onClick={() => handleClickLanguageItem("en")}
            >
              <span className={styles["language-flag"]}>🇺🇸</span>
              <span className={styles["language-name"]}>English</span>
            </li>
            <li
              className={styles["language-listitem"]}
              onClick={() => handleClickLanguageItem("de")}
            >
              <span className={styles["language-flag"]}>🇩🇪</span>
              <span className={styles["language-name"]}>Deutsch</span>
            </li>
          </ul>
          <div className={styles.dimmed} onMouseDown={() => setIsShowLanguageSwitcher(false)} />
        </>
      )}
    </div>
  );
};

export default LanguageSwitcher;

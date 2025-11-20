import useLanguageSwitcher from "../../hooks/useLanguageSwitcher";
import styles from "./LanguageSwitcher.module.css";

const LanguageSwitcher = () => {
  const { selectLanguage } = useLanguageSwitcher();

  return (
    <div className={styles["language-switcher"]}>
      <div className={styles["google-translate-hidden"]} id="google_translate_element" />
      <button className={styles.lang} onClick={() => selectLanguage("ko")} translate={"no"}>
        🇰🇷 KO
      </button>
      <button className={styles.lang} onClick={() => selectLanguage("en")} translate={"no"}>
        🇺🇸 EN
      </button>
      <button className={styles.lang} onClick={() => selectLanguage("de")} translate={"no"}>
        🇩🇪 DE
      </button>
    </div>
  );
};

export default LanguageSwitcher;

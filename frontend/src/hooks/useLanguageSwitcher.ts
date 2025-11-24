import { useEffect, useRef } from "react";
import { SupportedLanguage } from "../../types/domains/language";

const useLanguageSwitcher = () => {
  const timeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const selectLanguage = (language: SupportedLanguage, retries = 5) => {
    const languageDropdown = document.querySelector<HTMLSelectElement>(".goog-te-combo");

    if (languageDropdown) {
      languageDropdown.value = language;
      languageDropdown.dispatchEvent(new Event("change"));
      return;
    }

    if (retries > 0) {
      timeoutRef.current = setTimeout(() => selectLanguage(language, retries - 1), 1000);
    } else {
      alert("Google Translate failed to load. Please try again later.");
    }
  };

  useEffect(() => {
    const language = (navigator.language || "en").split("-")[0];

    if (language !== "ko") {
      selectLanguage("en");
    }

    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  return { selectLanguage };
};

export default useLanguageSwitcher;

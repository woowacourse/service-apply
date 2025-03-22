type SupportedLanguage = "en" | "ko" | "de";

const useLanguageSwitcher = () => {
  const selectLanguage = (language: SupportedLanguage, retries = 5) => {
    const languageDropdown = document.querySelector<HTMLSelectElement>(".goog-te-combo");

    if (!languageDropdown) {
      if (retries > 0) {
        setTimeout(() => selectLanguage(language, retries - 1), 1000);
      } else {
        alert("Google Translate failed to load. Please try again later.");
      }
      return;
    }

    languageDropdown.value = language;
    languageDropdown.dispatchEvent(new Event("change"));
  };

  return { selectLanguage };
};

export default useLanguageSwitcher;

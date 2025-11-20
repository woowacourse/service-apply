import { useEffect } from "react";

const useGoogleTranslate = () => {
  useEffect(() => {
    const scriptId = "google-translate-script";

    if (document.getElementById(scriptId)) return;

    const addScript = document.createElement("script");
    addScript.id = scriptId;
    addScript.src = "//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit";
    document.body.appendChild(addScript);

    window.googleTranslateElementInit = () => {
      new window.google.translate.TranslateElement(
        {
          pageLanguage: "ko",
          includedLanguages: "en,ko,de",
          layout: window.google.translate.TranslateElement.InlineLayout.HORIZONTAL,
        },
        "google_translate_element"
      );
    };
  }, []);
};

export default useGoogleTranslate;

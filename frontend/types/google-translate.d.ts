interface GoogleTranslateElementOptions {
  pageLanguage: string;
  includedLanguages: string;
  layout: number;
}

interface GoogleTranslateElement {
  new (options: GoogleTranslateElementOptions, elementId: string): void;
  InlineLayout: {
    HORIZONTAL: number;
    VERTICAL: number;
    SIMPLE: number;
  };
}

interface GoogleTranslate {
  TranslateElement: GoogleTranslateElement;
}

interface Google {
  translate: GoogleTranslate;
}

declare global {
  interface Window {
    google: Google;
    googleTranslateElementInit?: () => void;
  }
}

export {};

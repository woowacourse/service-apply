export const PHONE_NUMBER_HYPHEN_IDX = [3, 7];

export const formatHyphen = (str, firstHyphenIdx, secondHyphenIdx) =>
  str
    .replaceAll(/[^0-9]|-/g, "")
    .split("")
    .map((chr, idx) => (idx === firstHyphenIdx || idx === secondHyphenIdx ? ["-", chr] : chr))
    .flat()
    .join("");

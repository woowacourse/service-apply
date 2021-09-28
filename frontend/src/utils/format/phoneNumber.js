export const formatHyphen = (str, firstHyphenIdx, secondHyphenIdx) =>
  str
    .replaceAll("-", "")
    .split("")
    .map((el, idx) =>
      idx === firstHyphenIdx || idx === secondHyphenIdx ? ["-", el] : el
    )
    .flat()
    .join("");

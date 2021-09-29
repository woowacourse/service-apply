export const formatHyphen = (str, firstHyphenIdx, secondHyphenIdx) =>
  str
    .replaceAll("-", "")
    .split("")
    .map((chr, idx) =>
      idx === firstHyphenIdx || idx === secondHyphenIdx ? ["-", chr] : chr
    )
    .flat()
    .join("");

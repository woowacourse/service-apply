export const sanitizeString = (v: string) => {
  return v
    .trim()
    .replace(/\u001d/g, "")
    .replace(/\s+/g, " ");
};

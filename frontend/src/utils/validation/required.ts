export const hasValue = (v: unknown) => (typeof v === "string" ? !!v.trim() : !!v);

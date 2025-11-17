const URL_REGEX = /^https?:\/\/\S+$/i;

export const isValidUrl = (v: string) => v === "" || URL_REGEX.test(v);

const REGEX = /^[a-z\d](?:[a-z\d]|-(?=[a-z\d])){0,38}$/i;

export const isValidGithubUsername = (v: string) => REGEX.test(v);

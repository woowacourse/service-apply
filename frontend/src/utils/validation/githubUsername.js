const REGEX = /^[a-z\d](?:[a-z\d]|-(?=[a-z\d])){0,38}$/i;

export const isValidGithubUsername = (v) => REGEX.test(v);

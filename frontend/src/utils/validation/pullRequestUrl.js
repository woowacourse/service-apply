const REGEX = /^https:\/\/github\.com(\/[\w-]+){2}\/pull\/[1-9]\d*$/;

export const isValidPullRequestUrl = (v) => REGEX.test(v);

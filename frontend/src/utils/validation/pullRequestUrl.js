const REGEX = /^(https:\/\/github\.com\/)([a-z0-9\-\w]+\/*)+(pull\/)[0-9]+/;

export const isValidPullRequestUrl = (v) => REGEX.test(v);

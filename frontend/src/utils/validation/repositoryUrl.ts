const REGEX = /^https:\/\/github\.com(\/[\w-]+){2}\/?$/;

export const isValidRepositoryUrl = (v: string) => REGEX.test(v);

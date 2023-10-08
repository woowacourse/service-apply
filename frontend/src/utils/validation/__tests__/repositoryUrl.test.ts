import { isValidRepositoryUrl } from "../repositoryUrl";

describe("GitHub Repository Url 유효성 검사", () => {
  describe("유효한 url", () => {
    test("https://github.com/woowacourse/java-baseball/", () => {
      const url = "https://github.com/woowacourse/java-baseball/";

      const isValid = isValidRepositoryUrl(url);

      expect(isValid).toBe(true);
    });

    test("https://github.com/woowacourse/java-baseball", () => {
      const url = "https://github.com/woowacourse/java-baseball";

      const isValid = isValidRepositoryUrl(url);

      expect(isValid).toBe(true);
    });
  });

  describe("유효하지 않은 url", () => {
    test("https://github.com/woowacourse/java-baseball/pulls", () => {
      const url = "https://github.com/woowacourse/java-baseball/pulls";

      const isValid = isValidRepositoryUrl(url);

      expect(isValid).toBe(false);
    });

    test("https://github.com/woowacourse/", () => {
      const url = "https://github.com/woowacourse/";

      const isValid = isValidRepositoryUrl(url);

      expect(isValid).toBe(false);
    });
  });
});

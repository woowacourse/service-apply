import { isValidRepositoryUrl } from "../repositoryUrl";

describe("GitHub Repository URL 유효성 검사", () => {
  describe("유효한 URL", () => {
    test("/로 끝나는 유효한 URL", () => {
      const url = "https://github.com/woowacourse/java-baseball/";

      const isValid = isValidRepositoryUrl(url);

      expect(isValid).toBe(true);
    });

    test("/로 끝나지 않는 유효한 URL", () => {
      const url = "https://github.com/woowacourse/java-baseball";

      const isValid = isValidRepositoryUrl(url);

      expect(isValid).toBe(true);
    });
  });

  describe("유효하지 않은 URL", () => {
    test("Repository가 아닌 하위 경로를 포함하는 URL", () => {
      const url = "https://github.com/woowacourse/java-baseball/pulls";

      const isValid = isValidRepositoryUrl(url);

      expect(isValid).toBe(false);
    });

    test("특정 Repository가 지정되지 않은 URL", () => {
      const url = "https://github.com/woowacourse/";

      const isValid = isValidRepositoryUrl(url);

      expect(isValid).toBe(false);
    });
  });
});

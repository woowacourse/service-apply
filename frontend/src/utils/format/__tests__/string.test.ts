import { sanitizeString } from "../string";

describe("sanitizeString", () => {
  test.each([
    { input: "  Hello World  ", expected: "Hello World", description: "앞뒤 공백 제거" },
    { input: "Hello    World", expected: "Hello World", description: "연속된 공백 정규화" },
    { input: "Hello\u001dWorld", expected: "HelloWorld", description: "특수 문자 제거" },
    { input: "  Hello\u001d   World  ", expected: "Hello World", description: "모든 케이스 조합" },
    { input: "   ", expected: "", description: "공백만 있는 경우" },
    { input: "", expected: "", description: "빈 문자열" },
  ])("$description", ({ input, expected }) => {
    // when
    const actual = sanitizeString(input);

    // then
    expect(actual).toBe(expected);
  });
});

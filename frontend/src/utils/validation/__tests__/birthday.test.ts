// birthday.test.ts
import { isAtLeast14YearsOld } from "../birthday";

describe("isAtLeast14YearsOld 함수 테스트", () => {
  test("기준일로부터 생년월일이 만 14년을 초과한 경우, 만 14세 이상이어야 한다.", () => {
    // given
    const birthDate = "2009-05-27";
    const givenDate = "2024-05-27";

    // when
    const result = isAtLeast14YearsOld(birthDate, givenDate);

    // then
    expect(result).toBe(true);
  });

  test("기준일로부터 생년월일이 정확히 만 14년인 경우, 만 14세 이상이어야 한다.", () => {
    // given
    const birthDate = "2010-05-27";
    const givenDate = "2024-05-27";

    // when
    const result = isAtLeast14YearsOld(birthDate, givenDate);

    // then
    expect(result).toBe(true);
  });

  test("기준일로부터 생년월일이 만 14년이 되지 않은 경우, 만 14세 미만이어야 한다.", () => {
    // given
    const birthDate = "2010-05-28";
    const givenDate = "2024-05-27";

    // when
    const result = isAtLeast14YearsOld(birthDate, givenDate);

    // then
    expect(result).toBe(false);
  });
});

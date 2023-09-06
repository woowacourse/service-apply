import { describe, expect, test } from "@jest/globals";
import { PHONE_NUMBER_HYPHEN_IDX, formatHyphen } from "../phoneNumber";

describe("Format phoneNumber", () => {
  test("01012345678 -> 010-1234-5678", () => {
    const [firstHyphenIdx, secondHyphenIdx] = PHONE_NUMBER_HYPHEN_IDX;

    const formattedNumber = formatHyphen("01012345678", firstHyphenIdx, secondHyphenIdx);

    expect(formattedNumber).toBe("010-1234-5678");
  });
});

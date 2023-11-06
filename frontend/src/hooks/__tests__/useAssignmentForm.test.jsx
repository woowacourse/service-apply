import { act, renderHook } from "@testing-library/react";
import { describe, expect, test } from "@jest/globals";

import { MISSION_SUBMISSION_METHOD } from "../../constants/recruitment";
import { ERROR_MESSAGE } from "../../constants/messages";

import useAssignmentForm, { ASSIGNMENT_FORM_NAME } from "../useAssignmentForm";

describe("useAssignmentForm", () => {
  test("처음 호출했을 때 AssignmentForm의 초기값을 설정해야 한다.", () => {
    // given
    const submissionMethod = MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST;

    // when
    const { result } = renderHook(() => useAssignmentForm(submissionMethod));

    // then
    expect(result.current.form).toEqual({
      githubUsername: "",
      url: "",
      note: "",
    });
  });

  describe("SubmissionMethod가 PUBLIC_PULL_REQUEST인 경우", () => {
    const submissionMethod = MISSION_SUBMISSION_METHOD.PUBLIC_PULL_REQUEST;

    test("과제 제출물 url로 Pull Request URL을 제출할 수 있어야 한다.", () => {
      // given
      const url = "https://github.com/woowacourse-precourse/java-lotto/pull/1";

      // when
      const { result } = renderHook(() => useAssignmentForm(submissionMethod));
      act(() => {
        result.current.handleChanges[ASSIGNMENT_FORM_NAME.URL]({ target: { value: url } });
      });

      // then
      expect(result.current.isValid).toBe(true);
      expect(result.current.errorMessage[ASSIGNMENT_FORM_NAME.URL]).toBe("");
    });

    test("과제 제출물 url로 GitHub Repository URL을 제출할 수 없어야 한다.", () => {
      // given
      const url = "https://github.com/woowacourse-precourse/java-lotto";

      // when
      const { result } = renderHook(() => useAssignmentForm(submissionMethod));
      act(() => {
        result.current.handleChanges[ASSIGNMENT_FORM_NAME.URL]({ target: { value: url } });
      });

      // then
      expect(result.current.isValid).toBe(false);
      expect(result.current.errorMessage[ASSIGNMENT_FORM_NAME.URL]).toBe(
        ERROR_MESSAGE.VALIDATION.PULL_REQUEST_URL
      );
    });
  });

  describe("SubmissionMethod가 PRIVATE_REPOSITORY인 경우", () => {
    const submissionMethod = MISSION_SUBMISSION_METHOD.PRIVATE_REPOSITORY;

    test("과제 제출물 url로 GitHub Repository URL을 제출할 수 있어야 한다.", () => {
      // given
      const url = "https://github.com/woowacourse-precourse/java-lotto";

      // when
      const { result } = renderHook(() => useAssignmentForm(submissionMethod));
      act(() => {
        result.current.handleChanges[ASSIGNMENT_FORM_NAME.URL]({ target: { value: url } });
      });

      // then
      expect(result.current.isValid).toBe(true);
      expect(result.current.errorMessage[ASSIGNMENT_FORM_NAME.URL]).toBe("");
    });

    test("과제 제출물 url로 Pull Request URL을 제출할 수 없어야 한다.", () => {
      // given
      const url = "https://github.com/woowacourse-precourse/java-lotto/pull/1";

      // when
      const { result } = renderHook(() => useAssignmentForm(submissionMethod));
      act(() => {
        result.current.handleChanges[ASSIGNMENT_FORM_NAME.URL]({ target: { value: url } });
      });

      // then
      expect(result.current.isValid).toBe(false);
      expect(result.current.errorMessage[ASSIGNMENT_FORM_NAME.URL]).toBe(
        ERROR_MESSAGE.VALIDATION.REPOSITORY_URL
      );
    });
  });

  describe("SubmissionMethod가 지정되지 않은 경우", () => {
    test("PUBLIC_PULL_REQUEST인 경우와 동일하게 동작해야 한다.", () => {
      // given
      const url = "https://github.com/woowacourse-precourse/java-lotto/pull/1";

      // when
      const { result } = renderHook(() => useAssignmentForm());
      act(() => {
        result.current.handleChanges[ASSIGNMENT_FORM_NAME.URL]({ target: { value: url } });
      });

      // then
      expect(result.current.isValid).toBe(true);
      expect(result.current.errorMessage[ASSIGNMENT_FORM_NAME.URL]).toBe("");
    });
  });
});

export const RECRUITMENT_STATUS = {
  RECRUITABLE: "RECRUITABLE",
  RECRUITING: "RECRUITING",
  UNRECRUITABLE: "UNRECRUITABLE",
  ENDED: "ENDED",
} as const;

export const MISSION_STATUS = {
  PREPARED: "PREPARED",
  IN_PROGRESS: "IN_PROGRESS",
  SUBMITTABLE: "SUBMITTABLE",
  SUBMITTING: "SUBMITTING",
  UNSUBMITTABLE: "UNSUBMITTABLE",
  ENDED: "ENDED",
} as const;

// TODO: 과제 도메인 상수 분리
export const MISSION_SUBMISSION_METHOD = {
  PUBLIC_PULL_REQUEST: "PUBLIC_PULL_REQUEST",
  PRIVATE_REPOSITORY: "PRIVATE_REPOSITORY",
} as const;

export const BUTTON_LABEL = {
  BEFORE_SUBMIT: "준비 중",
  EDIT: "수정하기",
  SUBMIT: "제출하기",
  UNSUBMITTABLE: "제출불가",
  COMPLETE: "제출완료",
  UNSUBMITTED: "미제출",
  APPLY: "과제 제출",
  REFRESH: "새로고침",
  JUDGMENT: "예제 테스트 실행",
} as const;

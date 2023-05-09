export const RECRUITMENT_STATUS = {
  RECRUITABLE: "RECRUITABLE",
  RECRUITING: "RECRUITING",
  UNRECRUITABLE: "UNRECRUITABLE",
  ENDED: "ENDED",
} as const;

export const MISSION_STATUS = {
  SUBMITTABLE: "SUBMITTABLE",
  SUBMITTING: "SUBMITTING",
  UNSUBMITTABLE: "UNSUBMITTABLE",
  ENDED: "ENDED",
} as const;

export const BUTTON_LABEL = {
  BEFORE_SUBMIT: "준비 중",
  EDIT: "수정하기",
  SUBMIT: "제출하기",
  UNSUBMITTABLE: "제출불가",
  COMPLETE: "제출완료",
  UNSUBMITTED: "미제출",
} as const;

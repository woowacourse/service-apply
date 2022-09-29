export const RECRUITS_TAB = {
  ALL: {
    name: "all",
    label: "전체",
  },
  RECRUITABLE: {
    name: "recruitable",
    label: "모집 예정",
  },
  RECRUITING: {
    name: "recruiting",
    label: "모집 중",
  },
  ENDED: {
    name: "ended",
    label: "모집 종료",
  },
} as const;

export const COURSE_TAB = {
  ALL: {
    name: "all",
    label: "전체",
  },
  WOOWA_TECH_COURSE: {
    name: "woowacourse",
    label: "우아한테크코스",
  },
  WOOWA_TECH_CAMP: {
    name: "woowacamp",
    label: "우아한테크캠프",
  },
  WOOWA_TECH_CAMP_PRO: {
    name: "woowacamppro",
    label: "우아한테크캠프 Pro",
  },
} as const;

export const RECRUITS_TAB_LIST = [
  RECRUITS_TAB.ALL,
  RECRUITS_TAB.RECRUITABLE,
  RECRUITS_TAB.RECRUITING,
  RECRUITS_TAB.ENDED,
] as const;

export const COURSE_TAB_LIST = [
  COURSE_TAB.ALL,
  COURSE_TAB.WOOWA_TECH_COURSE,
  COURSE_TAB.WOOWA_TECH_CAMP,
  COURSE_TAB.WOOWA_TECH_CAMP_PRO,
] as const;

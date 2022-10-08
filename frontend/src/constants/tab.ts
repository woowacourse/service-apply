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

export const PROGRAM_TAB = {
  ALL: {
    name: "all",
    label: "전체",
    description: "메인 소개",
  },
  WOOWA_TECH_COURSE: {
    name: "woowacourse",
    label: "우아한테크코스",
    description: "우아한테크코스 소개",
  },
  WOOWA_TECH_CAMP: {
    name: "woowacamp",
    label: "우아한테크캠프",
    description: "우아한테크캠프 소개",
  },
  WOOWA_TECH_CAMP_PRO: {
    name: "woowacamppro",
    label: "우아한테크캠프 Pro",
    description: "우아한테크캠프 Pro 소개",
  },
} as const;

export const RECRUITS_TAB_LIST = [
  RECRUITS_TAB.ALL,
  RECRUITS_TAB.RECRUITABLE,
  RECRUITS_TAB.RECRUITING,
  RECRUITS_TAB.ENDED,
] as const;

export const PROGRAM_TAB_LIST = [
  PROGRAM_TAB.ALL,
  PROGRAM_TAB.WOOWA_TECH_COURSE,
  PROGRAM_TAB.WOOWA_TECH_CAMP,
  PROGRAM_TAB.WOOWA_TECH_CAMP_PRO,
] as const;

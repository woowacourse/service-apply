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
    description: "",
  },
  WOOWA_TECH_COURSE: {
    name: "woowacourse",
    label: "우아한테크코스",
    description: `일반 사용자용 서비스를 개발하는 회사가 필요로 하는 역량을 가진 개발자를 양성하기 위한 프로그램입니다.\n자기주도 학습, 현장 중심 경험, 깊이 있는 협업을 통해 성장하실 분들을 찾고 있어요.`,
  },
  WOOWA_TECH_CAMP_PRO: {
    name: "woowacamppro",
    label: "우아한테크캠프 Pro",
    description: `경력이 있는 재직자를 대상으로 유지보수하기 좋은 코드를 학습하는 교육 프로그램입니다.\n일반 사용자용 서비스 회사가 필요로 하는 역량을 더 쌓고 싶은 분들을 찾고 있어요.`,
  },
  EMPTY_PROGRAM: {
    name: "emptyprogram",
    label: "Empty State UI 확인용",
    description: "...",
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
  PROGRAM_TAB.WOOWA_TECH_CAMP_PRO,
  PROGRAM_TAB.EMPTY_PROGRAM,
] as const;

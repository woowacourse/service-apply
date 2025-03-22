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
    label: "우아한테크코스(Woowa Tech Course)",
    description: `일반 사용자용 서비스를 개발하는 회사가 필요로 하는 역량을 가진 개발자를 양성하기 위한 프로그램입니다. 자기주도 학습, 현장 중심 경험, 깊이 있는 협업을 통해 성장하실 분들을 찾고 있어요.`,
  },
  HERO_TECH_COURSE: {
    name: "herotechcourse",
    label: "히어로테크코스(Hero Tech Course)",
    description: `우아한테크코스(Woowa Tech Course)의 교육 철학을 기반으로, 글로벌 환경에서 성장할 개발자를 양성하는 교육 프로그램입니다.`,
  },
  WOOWA_TECH_CAMP_PRO: {
    name: "woowacamppro",
    label: "우아한테크캠프 Pro(Woowa Tech Camp Pro)",
    description: `경력이 있는 재직자를 대상으로 유지보수하기 좋은 코드를 학습하는 교육 프로그램입니다. 일반 사용자용 서비스 회사가 필요로 하는 역량을 더 쌓고 싶은 분들을 찾고 있어요.`,
  },
} as const;

export const PROGRAM_TAB_LIST = [
  PROGRAM_TAB.ALL,
  PROGRAM_TAB.WOOWA_TECH_COURSE,
  PROGRAM_TAB.HERO_TECH_COURSE,
] as const;

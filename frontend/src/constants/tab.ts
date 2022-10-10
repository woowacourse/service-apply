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
    description: `서비스를 개발하는 회사가 필요로 하는 역량을 가진 개발자 양성을 위한 프로그램입니다.\n자기주도 학습, 현장중심 경험, 깊이있는 협업을 함께하실 분들을 찾고 있어요.`,
  },
  WOOWA_TECH_CAMP: {
    name: "woowacamp",
    label: "우아한테크캠프",
    description: `다양한 미니 프로젝트를 통해 실제 서비스를 만들며 개발 지식을 익히는 교육형 인턴 과정입니다.\n프로그래밍 기본 지식과 개발자로 살고 싶은 열정이 있는 분들을 찾고 있어요.`,
  },
  WOOWA_TECH_CAMP_PRO: {
    name: "woowacamppro",
    label: "우아한테크캠프 Pro",
    description: `만 3년 이상 경력이 있는 재직자를 대상으로 하는 교육 과정 프로그램입니다.\n유지보수하기 좋은 코드를 실현하고 싶은, 서비스 회사가 필요로 하는 역량을 더 쌓고 싶은, 한 단계 더 성장하고 싶은 분들을 찾고 있어요.`,
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

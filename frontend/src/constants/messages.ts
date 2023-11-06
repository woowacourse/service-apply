export const ERROR_MESSAGE = {
  VALIDATION: {
    EMAIL: "정확한 이메일 양식을 입력해 주세요.",
    NAME: "정확한 한글 이름을 입력해 주세요.",
    PASSWORD: "영문, 숫자, 특수문자 포함하여 8자에서 20자 이내로 입력해 주세요.",
    PASSWORD_CAPSLOCK: "CapsLock이 켜져 있습니다.",
    CONFIRM_PASSWORD: "비밀번호를 확인해 주세요.",
    PHONE_NUMBER: "정확한 전화번호를 입력해 주세요. ex) 010-1234-5678",
    REQUIRED: "필수 정보입니다.",
    URL: "http(s)를 포함한 정확한 URL을 입력해 주세요.",
    TIMEOUT_EMAIL_AUTHENTICATION_CODE:
      "이메일 인증 코드 유효시간이 초과하였습니다. 이메일 인증 코드를 재발급해 주세요.",
    PULL_REQUEST_URL:
      "정확한 Pull Request 주소를 입력해 주세요. ex) https://github.com/woowacourse/java-baseball/pull/1",
    REPOSITORY_URL:
      "정확한 Repository 주소를 입력해 주세요. ex) https://github.com/javajigi/java-baseball-javajigi",
    GITHUB_USERNAME: "GitHub ID는 영어와 숫자 문자만 입력할 수 있습니다.",
    BIRTHDAY: "유효하지 않은 날짜입니다. 정확한 날짜를 입력해 주세요.",
  },
  API: {
    ALREADY_REGISTER: "이미 지원한 이력이 있습니다.",
    FETCHING_MY_APPLICATIONS: "내 지원 정보를 불러올 수 없습니다.",
    FETCHING_MY_APPLICATION: "내 지원서를 불러올 수 없습니다.",
    JOIN_FAILURE: "회원가입에 실패했습니다. 잠시 후 다시 시도해 주세요.",
    LOGIN_FAILURE: "아이디(E-mail) 또는 비밀번호를 확인해 주세요.",
    EDIT_FAILURE: "변경에 실패했습니다. 잠시 후 다시 시도해 주세요.",
    TOKEN_EXPIRED: "로그인 정보가 만료되었습니다. 다시 로그인해 주세요.",
    FETCHING_USER_INFO: "내 정보를 불러오는데 불러올 수 없습니다.",
    NOT_AUTHENTICATED: "이메일 인증을 완료해 주세요.",
    ALREADY_EXIST_EMAIL: "이미 가입된 이메일입니다.",
    INVALID_AUTHENTICATION_CODE: "인증 코드가 일치하지 않습니다.",
    SUBMIT_ASSIGNMENT: "과제를 제출할 수 없습니다.",
    SUBMIT_APPLICATION: "입력된 정보가 올바르지 않습니다. 다시 확인해 주세요.",
    FIND_PASSWORD: "입력된 정보가 올바르지 않습니다. 다시 확인해 주세요.",
    EDIT_PASSWORD: "입력된 정보가 올바르지 않습니다. 다시 확인해 주세요.",
    LOAD_APPLICATION_FORM: "지원서를 불러오는 데 실패했습니다. 잠시 후 다시 시도해 주세요.",
    SAVE_APPLICATION_FORM: "지원서를 저장하는 데 실패했습니다. 잠시 후 다시 시도해 주세요.",
  },
  ACCESS: {
    REQUIRED_LOGIN: "로그인이 필요합니다.",
  },
  HOOKS: {
    CANNOT_FIND_FORM_CONTEXT: "FormContext가 존재하지 않습니다.",
    CANNOT_FIND_RECRUITMENT_CONTEXT: "recruitmentContext가 존재하지 않습니다",
    CANNOT_FIND_TOKEN_CONTEXT: "TokenContext가 존재하지 않습니다",
    CANNOT_FIND_USER_INFO_CONTEXT: "UserInfoContext가 존재하지 않습니다",
    CANNOT_FIND_MODAL_CONTEXT: "ModalContext가 존재하지 않습니다",
  },
} as const;

export const SUCCESS_MESSAGE = {
  API: {
    CHANGE_PASSWORD: "비밀번호가 변경되었습니다. 다시 로그인해 주세요.",
    SUBMIT_APPLICATION: "정상적으로 제출되었습니다.",
    SUBMIT_ASSIGNMENT: "정상적으로 제출되었습니다.",
    SAVE_APPLICATION: "정상적으로 저장되었습니다.",
    EDIT_MY_PAGE: "정상적으로 변경되었습니다.",
  },
} as const;

export const CONFIRM_MESSAGE = {
  RESET_APPLICATION: "정말 초기화하시겠습니까?",
  CANCEL_ASSIGNMENT_SUBMIT: "정말 취소하시겠습니까? 작성하신 내용이 저장되지 않습니다.",
} as const;

export const PUBLIC_PULL_REQUEST_TOOLTIP_MESSAGE = [
  "예제 테스트 성적은 실제 성적과 무관합니다.",
  "제출 마감 후에는 예제 테스트를 실행할 수 없습니다.",
  "예제 테스트를 실행하지 않아도 제출된 Pull Request를 기반으로 채점을 진행합니다.",
  "과제 제출을 해야 예제 테스트를 실행할 수 있습니다.",
] as const;

export const PRIVATE_REPOSITORY_TOOLTIP_MESSAGE = [
  "예제 테스트 성적은 실제 성적과 무관합니다.",
  "제출 마감 후에는 예제 테스트를 실행할 수 없습니다.",
  "예제 테스트를 실행하지 않아도 제출한 저장소의 main 브랜치를 기반으로 채점을 진행합니다.",
  "과제 제출을 해야 예제 테스트를 실행할 수 있습니다.",
] as const;

export const ERROR_MESSAGE = {
  VALIDATION: {
    YEAR: "태어난 년도 4자리를 정확하게 입력해 주세요.",
    MONTH: "태어난 월을 정확하게 입력해 주세요.",
    DAY: "태어난 일을 정확하게 입력해 주세요.",
    EMAIL: "정확한 이메일 양식을 입력해 주세요.",
    NAME: "정확한 한글 이름을 입력해 주세요.",
    PASSWORD:
      "영문, 숫자, 특수문자 포함하여 8자에서 20자 이내로 입력해 주세요.",
    PASSWORD_CAPSLOCK: "CapsLock이 켜져 있습니다.",
    RE_PASSWORD: "비밀번호를 확인해주세요.",
    PHONE_NUMBER: "정확한 전화번호를 입력해 주세요. ex) 010-1234-5678",
    REQUIRED: "필수 정보입니다.",
    URL: "http(s)를 포함한 정확한 URL을 입력해 주세요.",
  },
  API: {
    ALREADY_REGISTER: "이미 지원한 이력이 있습니다.",
    FETCHING_MY_APPLICATION: "내 지원서를 불러오는데 실패했습니다.",
    JOIN_FAILURE: "회원가입에 실패했습니다. 잠시 후 다시 시도해주세요.",
    LOGIN_FAILURE: "아이디(E-mail) 또는 비밀번호를 확인해주세요.",
    EDIT_FAILURE: "변경에 실패했습니다. 잠시 후 다시 시도해주세요.",
    FETCHING_USER_INFO:
      "내 정보를 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요.",
    NOT_AUTHENTICATED: "이메일 인증을 완료해주세요.",
    ALREADY_EXIST_EMAIL: "이미 가입된 이메일입니다.",
    INVALID_AUTHENTICATION_CODE: "인증 코드가 일치하지 않습니다.",
  },
  ACCESS: {
    REQUIRED_LOGIN: "로그인이 필요합니다.",
  },
  HOOKS: {
    CANNOT_FIND_FORM_CONTEXT: "FormContext가 존재하지 않습니다.",
    CANNOT_FIND_RECRUITMENT_CONTEXT: "recruitmentContext가 존재하지 않습니다",
    CANNOT_FIND_TOKEN_CONTEXT: "TokenContext가 존재하지 않습니다",
    CANNOT_FIND_USER_INFO_CONTEXT: "UserInfoContext가 존재하지 않습니다",
  },
};

export const SUCCESS_MESSAGE = {
  API: {
    LOGIN: "로그인 성공",
    CHANGE_PASSWORD: "비밀번호가 변경되었습니다. 다시 로그인해주세요.",
    SUBMIT_APPLICATION: "정상적으로 제출되었습니다.",
    SAVE_APPLICATION: "정상적으로 저장되었습니다.",
    EDIT_MY_PAGE: "정상적으로 변경되었습니다.",
  },
};

export const CONFIRM_MESSAGE = {
  SUBMIT_APPLICATION:
    "제출하신 뒤에는 수정하실 수 없습니다. 정말로 제출하시겠습니까?",
  RESET_APPLICATION: "정말 초기화하시겠습니까?",
};
